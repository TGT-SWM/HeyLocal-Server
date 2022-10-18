/**
 * packageName    : com.heylocal.traveler.repository.redis
 * fileName       : RefreshTokenRedisRepository
 * author         : 우태균
 * date           : 2022/10/18
 * description    : RefreshToken 레디스 엔티티에 대한 레포지터리
 */

package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.redis.AccessToken;
import com.heylocal.traveler.domain.redis.RefreshToken;
import com.heylocal.traveler.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {
  private final RedisTemplate<String, String> redisTemplate;
  @Value("${jwt.token-valid-millisecond.refresh-token}")
  private long refreshTokenValidMilliSec;

  /**
   * RefreshToken 을 저장하는 메서드
   * @param refreshToken 저장할 토큰
   */
  public void save(RefreshToken refreshToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(refreshToken.getUserId());
    Map<String, String> mapRefreshToken = RefreshTokenMapper.INSTANCE.entityToMap(refreshToken);

    hashOperations.putAll(key, mapRefreshToken);
    redisTemplate.expire(key, refreshTokenValidMilliSec, TimeUnit.MILLISECONDS);
  }

  /**
   * ID 인 userId 값으로 RefreshToken을 조회하는 메서드
   * @param userId refreshToken의 id
   * @return
   */
  public Optional<RefreshToken> findByUserId(long userId) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(userId);
    Map<String, String> rawResult = hashOperations.entries(key);

    if (rawResult.size() == 0) {
      return Optional.empty();
    }

    //RefreshToken 으로 변환
    RefreshToken refreshToken = RefreshTokenMapper.INSTANCE.mapToEntity(rawResult);

    return Optional.of(refreshToken);
  }

  /**
   * redis 에서 제거하는 메서드
   * @param refreshToken 제거할 RefreshToken
   */
  public void remove(RefreshToken refreshToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(refreshToken.getUserId());
    Field[] declaredFields = RefreshToken.class.getDeclaredFields();

    for (Field field : declaredFields) {
      String fieldName = field.getName();
      hashOperations.delete(key, fieldName);
    }
  }

  /**
   * redis 에서 제거하는 메서드
   * @param userId 제거할 RefreshToken 의 id
   */
  public void removeByUserId(long userId) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(userId);
    Field[] declaredFields = RefreshToken.class.getDeclaredFields();

    for (Field field : declaredFields) {
      String fieldName = field.getName();
      hashOperations.delete(key, fieldName);
    }
  }

  /**
   * Redis Key 를 구하는 메서드
   * @param userId 특정 RefreshToken 의 userId 필드값
   * @return
   */
  private String getKey(long userId) {
    String key = "RefreshToken:" + userId;
    return key;
  }
}
