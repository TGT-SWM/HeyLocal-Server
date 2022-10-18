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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AccessTokenRedisRepository {
  private final RedisTemplate redisTemplate;

  /**
   * AccessToken 을 Redis에 저장하는 메서드
   * @param accessToken 저장할 토큰
   */
  public void save(AccessToken accessToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(accessToken.getUserId());
    Map<String, String> mapAccessToken = AccessTokenMapper.INSTANCE.entityToMap(accessToken);

    hashOperations.putAll(key, mapAccessToken);
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
