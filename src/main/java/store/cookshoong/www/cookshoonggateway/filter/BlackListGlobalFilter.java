package store.cookshoong.www.cookshoonggateway.filter;


import static store.cookshoong.www.cookshoonggateway.filter.AuthorizationGlobalFilter.extractToken;
import static store.cookshoong.www.cookshoonggateway.filter.AuthorizationGlobalFilter.isNoAuthenticationRequired;
import static store.cookshoong.www.cookshoonggateway.filter.AuthorizationGlobalFilter.obtainAuthorizationHeader;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import store.cookshoong.www.cookshoonggateway.exception.ExpiredTokenException;
import store.cookshoong.www.cookshoonggateway.service.TokenManagementService;

/**
 * 무효화된 토큰으로의 인증을 막는 필터.
 *
 * @author koesnam (추만석)
 * @since 2023.07.27
 */
@RequiredArgsConstructor
@Component
public class BlackListGlobalFilter implements GlobalFilter, Ordered {
    private final TokenManagementService tokenManagementService;

    @Override
    public int getOrder() {
        return FilterOrders.BLACK_LIST;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isNoAuthenticationRequired(request)) {
            return chain.filter(exchange);
        }
        String accessToken = obtainToken(request);
        if (tokenManagementService.isBlockedToken(accessToken)) {
            throw new ExpiredTokenException();
        }
        return chain.filter(exchange);
    }

    private static String obtainToken(ServerHttpRequest request) {
        return extractToken(obtainAuthorizationHeader(request));
    }
}
