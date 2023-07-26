package store.cookshoong.www.cookshoonggateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * properties 파일 설정.
 *
 * @author seungyeon
 * @since 2023/07/04
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cookshoong")
public class RouterProperties {
    private String backendPath;

    private String backendUrl;

    private String authPath;

    private String authUrl;

    private String deliveryPath;

    private String deliveryUrl;
}
