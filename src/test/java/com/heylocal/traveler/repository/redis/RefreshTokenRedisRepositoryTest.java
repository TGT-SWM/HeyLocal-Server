package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.token.RefreshToken;
import config.EmbeddedRedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmbeddedRedisConfig.class)
class RefreshTokenRedisRepositoryTest {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Test
  @DisplayName("저장")
  void saveTest() {
    //GIVEN
    long userId = 10L;
    RefreshToken saveTargetToken = getRefreshToken(userId);

    //WHEN
    refreshTokenRedisRepository.save(saveTargetToken);

    //THEN
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Map<String, String> entries = hashOperations.entries("RefreshToken:" + userId);
    assertAll(
        //성공 케이스
        () -> assertSame(5, entries.size()),
        () -> assertEquals(Long.toString(saveTargetToken.getUserId()), entries.get("userId")),
        () -> assertEquals(saveTargetToken.getTokenValue(), entries.get("tokenValue")),
        () -> assertEquals(Long.toString(saveTargetToken.getAccessTokenId()), entries.get("accessTokenId")),
        () -> assertEquals(saveTargetToken.getCreatedDate().toString(), entries.get("createdDate")),
        () -> assertEquals(Long.toString(saveTargetToken.getTimeoutSec()), entries.get("timeoutSec"))
    );
  }

  @Test
  @DisplayName("UserId(id) 로 조회")
  void findByUserIdTest() {
    //GIVEN
    long userId = 10L;
    RefreshToken targetToken = getRefreshToken(userId);

    //저장
    saveRefreshToken(targetToken);

    //WHEN
    Optional<RefreshToken> result = refreshTokenRedisRepository.findByUserId(userId);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(targetToken.getUserId(), result.get().getUserId()),
        () -> assertEquals(targetToken.getTokenValue(), result.get().getTokenValue()),
        () -> assertEquals(targetToken.getAccessTokenId(), result.get().getAccessTokenId()),
        () -> assertEquals(targetToken.getCreatedDate(), result.get().getCreatedDate()),
        () -> assertEquals(targetToken.getTimeoutSec(), result.get().getTimeoutSec())
    );
  }

  @Test
  @DisplayName("UserId(id) 로 삭제")
  void removeByUserIdTest() {
    //GIVEN
    long userId = 10L;
    RefreshToken targetToken = getRefreshToken(userId);

    //저장
    saveRefreshToken(targetToken);

    //WHEN
    refreshTokenRedisRepository.removeByUserId(userId);

    //THEN
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Map<String, String> entries = hashOperations.entries("RefreshToken:" + userId);

    //성공 케이스 - 제대로 제거가 되었는지 확인
    assertSame(0, entries.size());
  }

  private void saveRefreshToken(RefreshToken targetToken) {
    Map<String, String> mapToken = new ConcurrentHashMap<>();
    mapToken.put("userId", String.valueOf(targetToken.getUserId()));
    mapToken.put("tokenValue", targetToken.getTokenValue());
    mapToken.put("timeoutSec", String.valueOf(targetToken.getTimeoutSec()));
    mapToken.put("accessTokenId", String.valueOf(targetToken.getAccessTokenId()));
    mapToken.put("createdDate", targetToken.getCreatedDate().toString());
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    hashOperations.putAll("RefreshToken:" + targetToken.getUserId(), mapToken);
  }

  private RefreshToken getRefreshToken(long userId) {
    RefreshToken targetToken = RefreshToken.builder()
        .userId(userId)
        .tokenValue("tokenValue")
        .accessTokenId(1L)
        .createdDate(LocalDateTime.now())
        .timeoutSec(100000000L)
        .build();
    return targetToken;
  }
}