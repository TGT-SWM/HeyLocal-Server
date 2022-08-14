package com.heylocal.traveler.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class JwtTokenProviderTest {
  String secretKey = "H1jo5zWVaJiCiD6B/gpcdD0IspmEBDQ4Q0kxxUTkgU1ea97KF3hblwEHonHG6sy4KsLcv4u6IpfVI5WHcpg4unzhOZExuWCFTiVY2HQK5dnip2YPlCwPs4PeNAw/k2o6vJZvGn5HZ8whEOgqAtCvauxY8rIMptC9QUbh18B8bT5fZDEs5A5NzXx7YEUQuB3+TSI2xhpytDo6bIN3kSn/veuOdEc7DThkQ5xDZw==%";
  long accessTokenValidMilliSec = 7200000L;
  long refreshTokenValidMilliSec = 1210000000L;
  JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, accessTokenValidMilliSec, refreshTokenValidMilliSec);

  @Test
  @DisplayName("Access Token 생성")
  void createAccessTokenTest() {
    //GIVEN
    long userPk = 1L;
    String accountId = "testAccountId";
    String nickname = "testNickname";

    //WHEN
    String accessToken = jwtTokenProvider.createAccessToken(userPk, accountId, nickname);
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(accessToken)
        .getBody();

    //THEN
    assertAll(
      //성공 케이스 - 1
      () -> assertDoesNotThrow(() -> {
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(accessToken)
            .getBody();
      }),
      //성공 케이스 - 2
        () -> assertEquals(String.valueOf(userPk), String.valueOf(claims.get("userPk"))),
        () -> assertEquals(accountId, claims.get("accountId")),
        () -> assertEquals(nickname, claims.get("nickname")),
      //실패 케이스 - 1
        () -> assertThrows(Exception.class, () -> {
        Jwts.parser()
            .setSigningKey("invalid-secret-key")
            .parseClaimsJws(accessToken)
            .getBody();
      })
    );
  }

  @Test
  @DisplayName("Refresh Token 생성")
  void createRefreshTokenTest() {
    //GIVEN
    Date minExpiration = new Date(new Date().getTime() + refreshTokenValidMilliSec - 1000);

    //WHEN
    String refreshToken = jwtTokenProvider.createRefreshToken();
    Date expiration = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(refreshToken)
        .getBody().getExpiration();

    //THEN
    assertAll(
      //성공 케이스 - 1
      () -> assertDoesNotThrow(() -> {
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(refreshToken)
            .getBody();
      }),
      //성공 케이스 - 2
      () -> assertTrue(expiration.after(minExpiration)), //토큰 유효 기간이 minExpiration 보다 나중이어야 한다.
      //실패 케이스
      () -> assertThrows(Exception.class, () -> {
        Jwts.parser()
            .setSigningKey("invalid-secret-key")
            .parseClaimsJws(refreshToken);
      })
    );
  }
}