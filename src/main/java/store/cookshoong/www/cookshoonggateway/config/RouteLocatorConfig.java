package store.cookshoong.www.cookshoonggateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway에서 back, auth, delivery 경로 설정.
 *
 * @author seungyeon (유승연)
 * @since 2023/07/04
 * @contributor koesnam (추만석)
 */
@Configuration
@RequiredArgsConstructor
public class RouteLocatorConfig {
    public final RouterProperties routerProperties;

    /**
     * API 서버로 라우팅하는 주소들을 설정한다.
     *
     * @param builder the builder
     * @return the route locator
     */
    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("backend-server",
                p -> p.path(routerProperties.getBackendPath()).and()
                    .uri(routerProperties.getBackendUrl())
            )
            .route("auth-server",
                p -> p.path(routerProperties.getAuthPath()).and()
                    .uri(routerProperties.getAuthUrl())
            )
            .route("delivery-server",
                p -> p.path(routerProperties.getDeliveryPath()).and()
                    .uri(routerProperties.getDeliveryUrl())
            )
            .build();
    }
}
