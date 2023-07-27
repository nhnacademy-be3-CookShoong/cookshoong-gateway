package store.cookshoong.www.cookshoonggateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import store.cookshoong.www.cookshoonggateway.exception.InvalidHeaderException;
import store.cookshoong.www.cookshoonggateway.exception.InvalidTokenTypeException;
import store.cookshoong.www.cookshoonggateway.jwt.JwtParser;

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
    private static final int TOKEN_BEGIN_INDEX = 7;
    private final JwtParser jwtParser;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isAuthServerRequest(request) || isNoAuthenticationRequired(request)) {
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

    private static String obtainAuthorizationHeader(ServerHttpRequest request) {
        List<String> authorizationHeader = extractAuthorizationHeader(request);
        Assert.notNull(authorizationHeader, "Authorization 헤더가 존재하지 않습니다.");
        return authorizationHeader.get(0);
    }

    private static boolean isBearerType(String authorizationHeader) {
        return StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer");
    }

    private static boolean isAuthServerRequest(ServerHttpRequest request) {
        String requestPath = extractPath(request);
        return PATH_MATCHER.match("/auth/**", requestPath);
    }

    private static boolean hasOnlyOneAuthorizationHeader(ServerHttpRequest request) {
        List<String> authorizationHeader = extractAuthorizationHeader(request);
        Assert.notNull(authorizationHeader, "Authorization 헤더가 존재하지 않습니다.");
        return authorizationHeader.size() == 1;
    }

    private static boolean existsAuthorizationHeader(ServerHttpRequest request) {
        return extractAuthorizationHeader(request) != null;
    }

    private static boolean isNoAuthenticationRequired(ServerHttpRequest request) {
        String pathWithQuery = extractPath(request) + "?" + extractQuery(request);
        return Arrays.stream(ExcludedPattern.values())
            .anyMatch(excludedPattern -> PATH_MATCHER.match(excludedPattern.getPattern(), pathWithQuery));
    }

    private static List<String> extractAuthorizationHeader(ServerHttpRequest request) {
        return extractHeaders(request).get(AUTHORIZATION_HEADER);
    }

    private static HttpHeaders extractHeaders(ServerHttpRequest request) {
        return request.getHeaders();
    }

    private static String extractPath(ServerHttpRequest request) {
        return request.getPath().value();
    }

    private static String extractQuery(ServerHttpRequest request) {
        return request.getURI().getQuery();
    }

    private static String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(TOKEN_BEGIN_INDEX);
    }

    private void verifyAccess(String authorizationToken) {
        try {
            jwtParser.parseAccessToken(authorizationToken);
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e2) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다");
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
