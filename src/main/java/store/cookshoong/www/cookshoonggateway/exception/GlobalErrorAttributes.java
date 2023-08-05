package store.cookshoong.www.cookshoonggateway.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

/**
 * 예외 발생시의 응답 객체를 정의하는 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.23
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        ResponseStatusException error = (ResponseStatusException) this.getError(request);
        Map<String, Object> errorResponse = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        errorResponse.remove("path");
        errorResponse.remove("requestId");
        errorResponse.remove("error");
        errorResponse.put("message", error.getReason());
        return errorResponse;
    }
}
