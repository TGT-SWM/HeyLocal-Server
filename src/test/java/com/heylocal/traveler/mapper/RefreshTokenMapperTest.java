package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.token.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RefreshTokenMapperTest {
  private RefreshTokenMapper refreshTokenMapper = RefreshTokenMapper.INSTANCE;

  @Test
  @DisplayName("엔티티 -> Map")
  void entityToMapTest() throws IllegalAccessException {
    //GIVEN
    long userId = 1L;
    String tokenValue = "my token value";
    long accessTokenId = 1L;
    long timeoutSec = 100000L;
    LocalDateTime createdDate = LocalDateTime.now();
    RefreshToken accessToken = RefreshToken.builder()
        .userId(userId)
        .tokenValue(tokenValue)
        .accessTokenId(accessTokenId)
        .timeoutSec(timeoutSec)
        .createdDate(createdDate)
        .build();

    //WHEN
    Map<String, String> result = refreshTokenMapper.entityToMap(accessToken);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertEquals(String.valueOf(userId), result.get("userId")),
        () -> assertEquals(tokenValue, result.get("tokenValue")),
        () -> assertEquals(String.valueOf(accessTokenId), result.get("accessTokenId")),
        () -> assertEquals(String.valueOf(timeoutSec), result.get("timeoutSec")),
        () -> assertEquals(String.valueOf(createdDate), result.get("createdDate"))
    );
  }

  @Test
  @DisplayName("Map -> 엔티티")
  void mapToEntityTest() throws IllegalAccessException {
    //GIVEN
    String userId = "1";
    String tokenValue = "my token value";
    String accessTokenId = "1";
    String timeoutSec = "100000";
    String createdDate = LocalDateTime.now().toString();
    Map<String, String> entries = new ConcurrentHashMap<>();
    entries.put("userId", userId);
    entries.put("tokenValue", tokenValue);
    entries.put("accessTokenId", accessTokenId);
    entries.put("timeoutSec", timeoutSec);
    entries.put("createdDate", createdDate);

    //WHEN
    RefreshToken result = refreshTokenMapper.mapToEntity(entries);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertEquals(userId, String.valueOf(result.getUserId())),
        () -> assertEquals(tokenValue, result.getTokenValue()),
        () -> assertEquals(accessTokenId, String.valueOf(result.getAccessTokenId())),
        () -> assertEquals(timeoutSec, String.valueOf(result.getTimeoutSec())),
        () -> assertEquals(createdDate, result.getCreatedDate().toString())
    );
  }
}