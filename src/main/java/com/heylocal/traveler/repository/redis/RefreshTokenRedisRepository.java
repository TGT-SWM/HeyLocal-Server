/**
 * packageName    : com.heylocal.traveler.repository.redis
 * fileName       : RefreshTokenRedisRepository
 * author         : 우태균
 * date           : 2022/10/18
 * description    : RefreshToken 레디스 엔티티에 대한 레포지터리
 */

package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.redis.RefreshToken;
import com.heylocal.traveler.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {
  private final RedisTemplate<String, String> redisTemplate;

  /**
   * RefreshToken 을 저장하는 메서드
   * @param refreshToken 저장할 토큰
   */
  public void save(RefreshToken refreshToken) {
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    String key = getKey(refreshToken.getUserId());
    Map<String, String> mapRefreshToken = RefreshTokenMapper.INSTANCE.entityToMap(refreshToken);

    hashOperations.putAll(key, mapRefreshToken);
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
