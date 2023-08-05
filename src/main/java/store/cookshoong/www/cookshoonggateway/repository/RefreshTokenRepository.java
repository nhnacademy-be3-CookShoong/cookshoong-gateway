package store.cookshoong.www.cookshoonggateway.repository;

/**
 * Refresh 토큰을 레디스로부터 가져오기위한 Repository.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
public interface RefreshTokenRepository {
    void deleteByJti(String jti);

    boolean existsByJti(String jti);
}
