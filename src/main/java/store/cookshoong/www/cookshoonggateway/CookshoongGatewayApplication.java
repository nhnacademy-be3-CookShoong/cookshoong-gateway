package store.cookshoong.www.cookshoonggateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * gateway main.
 *
 * @author seungyeon
 * @since 2023/07/04
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class CookshoongGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookshoongGatewayApplication.class, args);
    }

}
