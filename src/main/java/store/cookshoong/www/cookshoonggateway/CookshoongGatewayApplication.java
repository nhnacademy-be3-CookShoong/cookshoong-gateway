package store.cookshoong.www.cookshoonggateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * gateway main.
 *
 * @author seungyeon
 * @since 2023/07/04
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableDiscoveryClient
public class CookshoongGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookshoongGatewayApplication.class, args);
    }

}
