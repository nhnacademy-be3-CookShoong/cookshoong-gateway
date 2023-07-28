package store.cookshoong.www.cookshoonggateway.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.cookshoong.www.cookshoonggateway.entity.BlockedToken;
import store.cookshoong.www.cookshoonggateway.jwt.JwtParser;
import store.cookshoong.www.cookshoonggateway.repository.BlockedTokenRepository;
import store.cookshoong.www.cookshoonggateway.repository.RefreshTokenRepository;

/**
 * 토큰값을 관리하는 서비스 계층.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
@Service
@RequiredArgsConstructor
public class TokenManagementService {
    private final JwtParser jwtParser;
    private final BlockedTokenRepository blockedTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 토큰값을 받아 해당 토큰을 무효화 시킨다.
     *
     * @param rawAccessToken the raw access token
     */
    public void addBlackList(String rawAccessToken) {
        Claims claims = jwtParser.parseAccessToken(rawAccessToken);
        String jti = (String) claims.get("jti");
        BlockedToken blockedToken = BlockedToken.convertFrom(rawAccessToken, claims);
        blockedTokenRepository.save(blockedToken);
        refreshTokenRepository.deleteByJti(jti);
    }

    public boolean isBlockedToken(String rawAccessToken) {
        String jti = (String) jwtParser.parseAccessToken(rawAccessToken).get("jti");
        return blockedTokenRepository.existsById(jti);
    }
}
