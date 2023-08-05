package store.cookshoong.www.cookshoonggateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 잘못된 서명이나 형식을 가지는 토큰이 인증을 시도할 때 발생하는 예외.
 *
 * @author koesnam (추만석)
 * @since 2023.07.27
 */
public class InvalidTokenException extends ResponseStatusException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");
    }
}
