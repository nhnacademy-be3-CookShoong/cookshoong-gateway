package store.cookshoong.www.cookshoonggateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import store.cookshoong.www.cookshoonggateway.exception.ExpiredTokenException;
import store.cookshoong.www.cookshoonggateway.exception.InvalidHeaderException;
import store.cookshoong.www.cookshoonggateway.exception.InvalidTokenException;
import store.cookshoong.www.cookshoonggateway.exception.InvalidTokenTypeException;
import store.cookshoong.www.cookshoonggateway.jwt.JwtParser;
import store.cookshoong.www.cookshoonggateway.util.RequestUtils;

/**
 * 게이트웨이를 통한 모든 요청들이(auth server 제외) 적용되는 필터.
 *
 * @author koesnam (추만석)
 * @since 2023.07.22
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationGlobalFilter implements GlobalFilter, Ordered {
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtParser jwtParser;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isNoAuthenticationRequired(request)) {
            return chain.filter(exchange);
        }
        if (!existsAuthorizationHeader(request) || !hasOnlyOneAuthorizationHeader(request)) {
            throw new InvalidHeaderException();
        }
        String header = obtainAuthorizationHeader(request);
        if (!isBearerType(header)) {
            throw new InvalidTokenTypeException();
        }

        verifyAccess(extractToken(header));
        return chain.filter(exchange);
    }

    protected static String obtainAuthorizationHeader(ServerHttpRequest request) {
        List<String> authorizationHeader = extractAuthorizationHeader(request);
        Assert.notNull(authorizationHeader, "Authorization 헤더가 존재하지 않습니다.");
        return authorizationHeader.get(0);
    }

    private static boolean isBearerType(String authorizationHeader) {
        return StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer");
    }

    private static boolean isAuthServerRequest(ServerHttpRequest request) {
        String requestPath = RequestUtils.extractPath(request);
        return PATH_MATCHER.match("/auth/**", requestPath);
    }

    private static boolean isExcludedPath(ServerHttpRequest request) {
        StringBuilder sb = new StringBuilder(RequestUtils.extractPath(request));
        if (RequestUtils.extractQuery(request) != null) {
            sb.append("?").append(RequestUtils.extractQuery(request));
        }

        return Arrays.stream(ExcludedPattern.values())
            .anyMatch(excludedPattern -> PATH_MATCHER.match(excludedPattern.getPattern(), sb.toString()));
    }

    private static boolean hasOnlyOneAuthorizationHeader(ServerHttpRequest request) {
        List<String> authorizationHeader = extractAuthorizationHeader(request);
        Assert.notNull(authorizationHeader, "Authorization 헤더가 존재하지 않습니다.");
        return authorizationHeader.size() == 1;
    }

    private static boolean existsAuthorizationHeader(ServerHttpRequest request) {
        return extractAuthorizationHeader(request) != null;
    }

    protected static boolean isNoAuthenticationRequired(ServerHttpRequest request) {
        return isAuthServerRequest(request) || isExcludedPath(request);
    }

    private static List<String> extractAuthorizationHeader(ServerHttpRequest request) {
        return RequestUtils.extractHeaders(request).get(AUTHORIZATION_HEADER);
    }

    protected static String extractToken(String authorizationHeader) {
        return authorizationHeader.split("\\s")[1];
    }

    private void verifyAccess(String authorizationToken) {
        try {
            jwtParser.parseAccessToken(authorizationToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e2) {
            throw new InvalidTokenException();
        }
    }

    private enum ExcludedPattern {
        RETRIEVE_ADDRESS_PATTERN("/api/accounts/customer/*/stores*"),
        ACCOUNT_REGISTRATION_PATTERN("/api/accounts?authorityCode=*");
        private final String pattern;

        ExcludedPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
