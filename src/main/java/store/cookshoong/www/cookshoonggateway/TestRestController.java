package store.cookshoong.www.cookshoonggateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {설명을 작성해주세요}
 *
 * @author seungyeon
 * @since 2023/07/03
 */
@RestController
public class TestRestController {
    @GetMapping
    public String testOn(){
        return "test-gateway";
    }
}
