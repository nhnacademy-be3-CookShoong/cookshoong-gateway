package store.cookshoong.www.cookshoonggateway.entity;

import io.jsonwebtoken.Claims;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * 무효화된 토큰을 관리하기 위한 블랙리스트 엔터티.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
@Getter
@RedisHash(value = "blocked_tokens")
@AllArgsConstructor
public class BlockedToken {
    @Id
    private String jti;
    private String rawAccessToken;
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expireTime;

    /**
     * 문자열 형태의 토큰을 엔터티 객체로 변환시켜준다.
     *
     * @param rawAccessToken the raw access token
     * @param claims         the claims
     * @return the blocked token
     */
    public static BlockedToken convertFrom(String rawAccessToken, Claims claims) {
        String jti = (String) claims.get("jti");
        Long expireTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        return new BlockedToken(jti, rawAccessToken, expireTime);
    }
}
