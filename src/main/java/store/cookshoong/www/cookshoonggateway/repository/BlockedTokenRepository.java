package store.cookshoong.www.cookshoonggateway.repository;

import org.springframework.data.repository.CrudRepository;
import store.cookshoong.www.cookshoonggateway.entity.BlockedToken;

/**
 * 무효화 된 토큰을 저장하기 위한 레포지토리. (블랙리스트)
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
public interface BlockedTokenRepository extends CrudRepository<BlockedToken, String> {
}
