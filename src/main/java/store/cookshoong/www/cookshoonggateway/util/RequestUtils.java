package store.cookshoong.www.cookshoonggateway.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Request 로부터 필요한 Uri 요소들을 가져오기위한 유틸 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.27
 */
public class RequestUtils {
    private RequestUtils() {}

    public static HttpHeaders extractHeaders(ServerHttpRequest request) {
        return request.getHeaders();
    }

    public static String extractPath(ServerHttpRequest request) {
        return request.getPath().value();
    }

    public static String extractQuery(ServerHttpRequest request) {
        return request.getURI().getQuery();
    }
}
