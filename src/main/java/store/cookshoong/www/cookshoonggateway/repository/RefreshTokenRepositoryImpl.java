package store.cookshoong.www.cookshoonggateway.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * 리프레쉬 토큰 검증과 무효화된 리프레쉬 토큰을 삭제해주기 위한 레포지토리.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "refresh_tokens:";

    @Override
    public void deleteByJti(String jti) {
        redisTemplate.delete(KEY_PREFIX + jti);
    }

    @Override
    public boolean existsByJti(String jti) {
        return !redisTemplate.opsForHash().keys(KEY_PREFIX + jti).isEmpty();
    }
}
