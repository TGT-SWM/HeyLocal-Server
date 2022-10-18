/**
 * packageName    : com.heylocal.traveler.config
 * fileName       : RedisConfig
 * author         : 우태균
 * date           : 2022/10/17
 * description    : Redis Client 빈 등록 Configuration
 */

package com.heylocal.traveler.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    redisTemplate.setDefaultSerializer(new StringRedisSerializer()); //직렬화 방식

    redisTemplate.setEnableTransactionSupport(true); //트랜잭션 처리 설정
    return redisTemplate;
  }
}
