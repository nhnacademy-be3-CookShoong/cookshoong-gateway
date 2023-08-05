package store.cookshoong.www.cookshoonggateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import store.cookshoong.www.cookshoonggateway.service.TokenManagementService;

/**
 * Front 에서 로그아웃시 토큰을 무효화하기 위해 요청되는 컨트롤러.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BlackListController {
    private final TokenManagementService tokenManagementService;

    /**
     * 무효화된 토큰을 블랙리스트에 추가한다. (무효된 토큰으로 인증한는 것을 막기위함)
     *
     * @param token the token
     * @return the response entity
     */
    @GetMapping("/token-invalidate")
    public ResponseEntity<Void> getAddTokenInBlackList(@RequestHeader("Authorization") String token) {
        String rawAccessToken = token.split("\\s")[1];
        tokenManagementService.addBlackList(rawAccessToken);
        return ResponseEntity.ok()
            .build();
    }
}
