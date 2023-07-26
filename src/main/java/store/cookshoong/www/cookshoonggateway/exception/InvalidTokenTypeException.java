package store.cookshoong.www.cookshoonggateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 정해둔 토큰 타입외에 토큰을 입력받았을 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.23
 */
public class InvalidTokenTypeException extends ResponseStatusException {
    public InvalidTokenTypeException() {
        super(HttpStatus.UNAUTHORIZED, "토큰 타입을 확인해주세요.");
    }
}
