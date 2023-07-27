package store.cookshoong.www.cookshoonggateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 인증헤더가 정상적으로 들어오지 않았을 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.23
 */
public class InvalidHeaderException extends ResponseStatusException {
    public InvalidHeaderException() {
        super(HttpStatus.UNAUTHORIZED, "인증 헤더가 없거나 둘 이상입니다.");
    }
}
