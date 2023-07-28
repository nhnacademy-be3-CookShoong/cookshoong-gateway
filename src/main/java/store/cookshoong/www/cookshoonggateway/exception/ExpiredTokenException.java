package store.cookshoong.www.cookshoonggateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 만료된 토큰으로 인증을 시도할 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.27
 */
public class ExpiredTokenException extends ResponseStatusException {
    public ExpiredTokenException() {
        super(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
    }
}
