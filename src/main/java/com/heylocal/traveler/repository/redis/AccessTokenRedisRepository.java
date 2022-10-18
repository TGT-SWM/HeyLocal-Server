/**
 * packageName    : com.heylocal.traveler.repository.redis
 * fileName       : AccessTokenRedisRepository
 * author         : 우태균
 * date           : 2022/10/18
 * description    : AccessToken 레디스 엔티티에 대한 레포지터리
 */

package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.redis.AccessToken;
import com.heylocal.traveler.mapper.AccessTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AccessTokenRedisRepository {
  private final RedisTemplate redisTemplate;
  @Value("${jwt.token-valid-millisecond.refresh-token}")
  private long refreshTokenValidMilliSec;

  /**
   * AccessToken 을 Redis에 저장하는 메서드
   * @param accessToken 저장할 토큰
   */
  public void save(AccessToken accessToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(accessToken.getUserId());
    Map<String, String> mapAccessToken = AccessTokenMapper.INSTANCE.entityToMap(accessToken);

    hashOperations.putAll(key, mapAccessToken);

    /*
     * 토큰쌍(Access·Refresh)가 살아있을 수 있는 최대 시간은 Refresh 토큰의 만료일이므로,
     * refreshTokenValidMilliSec 로 설정한다.
     */
    redisTemplate.expire(key, refreshTokenValidMilliSec, TimeUnit.MILLISECONDS);
  }

  /**
   * ID 인 userId 값으로 AccessToken을 조회하는 메서드
   * @param userId accessToken 의 id
   * @return
   */
  public Optional<AccessToken> findByUserId(long userId) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(userId);
    Map<String, String> rawResult = hashOperations.entries(key);

    if (rawResult.size() == 0) {
      return Optional.empty();
    }

    //RefreshToken 으로 변환
    AccessToken accessToken = AccessTokenMapper.INSTANCE.mapToEntity(rawResult);

    return Optional.of(accessToken);
  }

  /**
   * redis 에서 제거하는 메서드
   * @param accessToken 제거할 AccessToken
   */
  public void remove(AccessToken accessToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(accessToken.getUserId());
    Field[] declaredFields = AccessToken.class.getDeclaredFields();

    for (Field field : declaredFields) {
      String fieldName = field.getName();
      hashOperations.delete(key, fieldName);
    }
  }

  /**
   * redis 에서 제거하는 메서드
   * @param userId 제거할 AccessToken 의 id
   */
  public void removeByUserId(long userId) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(userId);
    Field[] declaredFields = AccessToken.class.getDeclaredFields();

    for (Field field : declaredFields) {
      String fieldName = field.getName();
      hashOperations.delete(key, fieldName);
    }
  }

  /**
   * Redis Key 를 구하는 메서드
   * @param userId 특정 AccessToken 의 userId 필드값
   * @return
   */
  private String getKey(long userId) {
    String key = "AccessToken:" + userId;
    return key;
  }
}
