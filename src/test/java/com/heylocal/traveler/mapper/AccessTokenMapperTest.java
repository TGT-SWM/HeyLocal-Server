package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.token.AccessToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenMapperTest {
  private AccessTokenMapper accessTokenMapper = AccessTokenMapper.INSTANCE;

  @Test
  @DisplayName("엔티티 -> Map")
  void entityToMapTest() throws IllegalAccessException {
    //GIVEN
    long userId = 1L;
    String tokenValue = "my token value";
    long refreshTokenId = 1L;
    long timeoutSec = 100000L;
    LocalDateTime createdDate = LocalDateTime.now();
    AccessToken accessToken = AccessToken.builder()
        .userId(userId)
        .tokenValue(tokenValue)
        .refreshTokenId(refreshTokenId)
        .timeoutSec(timeoutSec)
        .createdDate(createdDate)
        .build();

    //WHEN
    Map<String, String> result = accessTokenMapper.entityToMap(accessToken);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertEquals(String.valueOf(userId), result.get("userId")),
        () -> assertEquals(tokenValue, result.get("tokenValue")),
        () -> assertEquals(String.valueOf(refreshTokenId), result.get("refreshTokenId")),
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
    String refreshTokenId = "1";
    String timeoutSec = "100000";
    String createdDate = LocalDateTime.now().toString();
    Map<String, String> entries = new ConcurrentHashMap<>();
    entries.put("userId", userId);
    entries.put("tokenValue", tokenValue);
    entries.put("refreshTokenId", refreshTokenId);
    entries.put("timeoutSec", timeoutSec);
    entries.put("createdDate", createdDate);

    //WHEN
    AccessToken result = accessTokenMapper.mapToEntity(entries);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertEquals(userId, String.valueOf(result.getUserId())),
        () -> assertEquals(tokenValue, result.getTokenValue()),
        () -> assertEquals(refreshTokenId, String.valueOf(result.getRefreshTokenId())),
        () -> assertEquals(timeoutSec, String.valueOf(result.getTimeoutSec())),
        () -> assertEquals(createdDate, result.getCreatedDate().toString())
    );
  }
}