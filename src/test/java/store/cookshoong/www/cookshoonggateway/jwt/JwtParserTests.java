package store.cookshoong.www.cookshoonggateway.jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Jwt 파싱 동작을 이해하기위한 학습용 테스트.
 *
 * @author koesnam (추만석)
 * @since 2023.07.22
 */
class JwtParserTests {
    @Test
    @DisplayName("파싱할 때 require 메서드는 특정필드의 특정값을 검증한다. (특정 필드가 있는지만 확인은 불가)")
    void parse() {
        String tempSecret = "fwelkfjewiogjdfkjbndkjg hneriowjelklkfjioerhgnerklgjsadksladjf";

        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
            .setClaims(Map.of("required", "1", "required2", "23412", "required3", System.currentTimeMillis()))
            .compact();

        String actual = (String) Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
            .require("required", "1")
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("required");

        System.out.println(actual);

        assertThat(actual, is("1"));
    }

    @Test
    @DisplayName("Jwt의 Claims에 담긴 Long 값은 파싱할 때 Integer로 인식된다.")
    void parse_2() {
        String tempSecret = "fwelkfjewiogjdfkjbndkjg hneriowjelklkfjioerhgnerklgjsadksladjf";

        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
            .setClaims(Map.of("required", 1L, "required2", "23412", "required3", System.currentTimeMillis()))
            .compact();

        try {
            Long temp = ((Long) Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
                .require("required", 1L)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("required"));
        } catch (ClassCastException ex) {
            System.out.println(ex.getMessage());
            Assertions.assertThat(ex).hasMessageContaining("Integer cannot be cast to class java.lang.Long");
        }
    }

    @Test
    @DisplayName("Jwt의 Claims에 담긴 Long 값은 파싱할 때 String으로 변환후 Long으로 변환이 가능하다")
    void parse_3() {
        String tempSecret = "fwelkfjewiogjdfkjbndkjg hneriowjelklkfjioerhgnerklgjsadksladjf";

        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
            .setClaims(Map.of("required", 1L, "required2", "23412", "required3", System.currentTimeMillis()))
            .compact();

        Long actual = Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(tempSecret.getBytes(StandardCharsets.UTF_8)))
            .require("required", 1L)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("required")
            .toString());

        assertThat(actual, is(1L));
    }
}
