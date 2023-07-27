package store.cookshoong.www.cookshoonggateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import store.cookshoong.www.cookshoonggateway.property.RedisProperties;
import store.cookshoong.www.cookshoonggateway.skm.SKMService;

/**
 * 레디스 관련 설정을 하기 위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.26
 */
@Configuration
public class RedisConfig {
    /**
     * skm 으로부터 레디스 설정정보를 가져온다.
     *
     * @param redisKeyid the redis keyid
     * @param skmService the skm service
     * @return the redis properties
     * @throws JsonProcessingException the json processing exception
     */
    @Bean
    @Profile("!default")
    public RedisProperties redisProperties(@Value("${cookshoong.skm.keyid.redis}") String redisKeyid,
                                           SKMService skmService) throws JsonProcessingException {
        return skmService.fetchSecrets(redisKeyid, RedisProperties.class);
    }

    /**
     * Redis Database 분리를 염두에 둔 token 용 RedisConnectionFactory.
     *
     * @param redisProperties the redis properties
     * @param database        the database
     * @return the redis connection factory
     */
    @Bean
    @Profile("!default")
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties,
                                                         @Value("${spring.redis.database}") Integer database) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setPassword(redisProperties.getPassword());
        configuration.setDatabase(database);
        return new LettuceConnectionFactory(configuration);
    }
}
