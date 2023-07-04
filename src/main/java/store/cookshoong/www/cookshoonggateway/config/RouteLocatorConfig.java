package store.cookshoong.www.cookshoonggateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway에서 back, auth, delivery 경로 설정
 *
 * @author seungyeon
 * @since 2023/07/04
 */
@Configuration
public class RouteLocatorConfig {
    @Value("${backend.path}")
    private String backendPath;

    @Value("${backend.url}")
    private String backendUrl;

    @Value("${auth.path}")
    private String authPath;

    @Value("${auth.url}")
    private String authUrl;

    @Value("${delivery.path}")
    private String deliveryPath;

    @Value("${delivery.url}")
    private String deliveryUrl;

    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("backend-server",
                p -> p.path(backendPath).and()
                    .uri(backendUrl)
            )
            .route("auth-server",
                p -> p.path(authPath).and()
                    .uri(authUrl)
            )
            .route("delivery-server",
                p -> p.path(deliveryPath).and()
                    .uri(deliveryUrl)
            )
            .build();
    }

}

