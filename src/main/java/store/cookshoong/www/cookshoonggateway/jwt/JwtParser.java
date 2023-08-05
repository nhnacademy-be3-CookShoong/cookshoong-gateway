package store.cookshoong.www.cookshoonggateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Jwt 에 대한 검증을 위한 파서.
 *
 * @author koesnam (추만석)
 * @since 2023.07.22
 */
@Component
@RequiredArgsConstructor
public class JwtParser {
    private final JwtProperties jwtProperties;

    /**
     * Access Token 에 대한 검증과 동시에 토큰의 담긴 정보를 추출한다.
     *
     * @param token the token
     * @return the claims
     */
    public Claims parseAccessToken(String token) {
        return parse(token, TokenType.ACCESS);
    }

    /**
     * Refresh Token 에 대한 검증과 동시에 토큰의 담긴 정보를 추출한다.
     *
     * @param token the token
     * @return the claims
     */
    public Claims parseRefreshToken(String token) {
        return parse(token, TokenType.REFRESH);
    }

    /**
     * 토큰의 타입별로 다른 키를 사용하여 토큰의 담긴 정보를 추출한다.
     *
     * @param token     the token
     * @param tokenType the token type
     * @return the claims
     */
    private Claims parse(String token, TokenType tokenType) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey(tokenType))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }


    private SecretKey getKey(TokenType tokenType) {
        String secret = "";
        switch (tokenType) {
            case ACCESS:
                secret = jwtProperties.getJwtSecret().getAccessSecret();
                break;
            case REFRESH:
                secret = jwtProperties.getJwtSecret().getRefreshSecret();
                break;
            default:
                Assert.hasLength(secret, "secret이 비어있습니다.");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private enum TokenType {
        ACCESS, REFRESH
    }
}

