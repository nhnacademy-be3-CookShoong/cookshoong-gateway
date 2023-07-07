package store.cookshoong.www.cookshoonggateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

/**
 * Gateway에서 back, auth, delivery 경로 설정.
 *
 * @author seungyeon
 * @since 2023/07/04
 */
@Configuration
@RequiredArgsConstructor
public class RouteLocatorConfig {
    public final RouterProperties routerProperties;

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
