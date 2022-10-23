package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.token.AccessToken;
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
class AccessTokenRedisRepositoryTest {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private AccessTokenRedisRepository accessTokenRedisRepository;

  @Test
  @DisplayName("저장")
  void saveTest() {
    //GIVEN
    long userId = 10L;
    AccessToken targetToken = getAccessToken(userId);

    //WHEN
    accessTokenRedisRepository.save(targetToken);

    //THEN
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Map<String, String> entries = hashOperations.entries("AccessToken:" + userId);

    assertAll(
        //성공 케이스
        () -> assertSame(5, entries.size()),
        () -> assertEquals(Long.toString(targetToken.getUserId()), entries.get("userId")),
        () -> assertEquals(targetToken.getTokenValue(), entries.get("tokenValue")),
        () -> assertEquals(Long.toString(targetToken.getRefreshTokenId()), entries.get("refreshTokenId")),
        () -> assertEquals(targetToken.getCreatedDate().toString(), entries.get("createdDate")),
        () -> assertEquals(Long.toString(targetToken.getTimeoutSec()), entries.get("timeoutSec"))
    );
  }

  @Test
  @DisplayName("UserId(id) 로 조회")
  void findByUserIdTest() {
    //GIVEN
    long userId = 10L;
    AccessToken targetToken = getAccessToken(userId);

    //저장
    saveAccessToken(targetToken);

    //WHEN
    Optional<AccessToken> result = accessTokenRedisRepository.findByUserId(userId);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(targetToken.getUserId(), result.get().getUserId()),
        () -> assertEquals(targetToken.getTokenValue(), result.get().getTokenValue()),
        () -> assertEquals(targetToken.getRefreshTokenId(), result.get().getRefreshTokenId()),
        () -> assertEquals(targetToken.getCreatedDate(), result.get().getCreatedDate()),
        () -> assertEquals(targetToken.getTimeoutSec(), result.get().getTimeoutSec())
    );
  }

  @Test
  @DisplayName("UserId(id) 로 제거")
  void removeByUserIdTest() {
    //GIVEN
    long userId = 10L;
    AccessToken targetToken = getAccessToken(userId);

    //저장
    saveAccessToken(targetToken);

    //WHEN
    accessTokenRedisRepository.removeByUserId(userId);

    //THEN
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    Map<String, String> entries = hashOperations.entries("AccessToken:" + userId);

    //성공 케이스 - 제대로 제거가 되었는지 확인
    entries.entrySet().stream().forEach((item) -> System.out.println(item.getKey()));
    assertSame(0, entries.size());
  }

  private void saveAccessToken(AccessToken targetToken) {
    Map<String, String> mapToken = new ConcurrentHashMap<>();
    mapToken.put("userId", String.valueOf(targetToken.getUserId()));
    mapToken.put("tokenValue", targetToken.getTokenValue());
    mapToken.put("timeoutSec", String.valueOf(targetToken.getTimeoutSec()));
    mapToken.put("refreshTokenId", String.valueOf(targetToken.getRefreshTokenId()));
    mapToken.put("createdDate", targetToken.getCreatedDate().toString());
    HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
    hashOperations.putAll("AccessToken:" + targetToken.getUserId(), mapToken);
  }

  private AccessToken getAccessToken(long userId) {
    AccessToken targetToken = AccessToken.builder()
        .userId(userId)
        .tokenValue("tokenValue")
        .refreshTokenId(1L)
        .createdDate(LocalDateTime.now())
        .timeoutSec(100000000L)
        .build();
    return targetToken;
  }
}