package com.heylocal.traveler.util.jwt;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenParserTest {
  String secretKey = "H1jo5zWVaJiCiD6B/gpcdD0IspmEBDQ4Q0kxxUTkgU1ea97KF3hblwEHonHG6sy4KsLcv4u6IpfVI5WHcpg4unzhOZExuWCFTiVY2HQK5dnip2YPlCwPs4PeNAw/k2o6vJZvGn5HZ8whEOgqAtCvauxY8rIMptC9QUbh18B8bT5fZDEs5A5NzXx7YEUQuB3+TSI2xhpytDo6bIN3kSn/veuOdEc7DThkQ5xDZw==%";
  JwtTokenParser jwtTokenParser = new JwtTokenParser(secretKey);

  @Test
  @DisplayName("토큰 유효성 검증 및 클레임 파싱")
  void parseJwtTokenTest() {
    //GIVEN
    Date now = new Date();
    long tokenValidMilliSec = 10000000000000L;
    String claimName = "testName";
    String claimValue = "testValue";
    String validToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("test")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidMilliSec))
        .claim(claimName, claimValue)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
    String expiredToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("test")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() - 1000))
        .claim(claimName, claimValue)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
    String validAuthHttpHeader = "Bearer " + validToken;
    String expiredAuthHttpHeader = "Bearer " + expiredToken;
    String wrongFormatAuthHttpHeader = validToken;
    String wrongValueAuthHttpHeader = "Bearer " + "this is not token value";

    //WHEN
    Optional<Claims> claimsOptional = jwtTokenParser.parseJwtToken(validAuthHttpHeader);

    //THEN
    assertAll(
      //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> claimsOptional.get()),
      //성공 케이스 - 2
        () -> assertEquals(claimValue, claimsOptional.get().get(claimName)),
      //실패 케이스 - 1 - 유효기간이 만료된 경우
        () -> assertThrows(ExpiredJwtException.class, () -> jwtTokenParser.parseJwtToken(expiredAuthHttpHeader)),
      //실패 케이스 - 2 - 잘못된 Authorization HTTP 헤더 형식 (Bearer 가 없는) 으로 값이 전달된 경우
        () -> assertThrows(JwtException.class, () -> jwtTokenParser.parseJwtToken(wrongFormatAuthHttpHeader)),
      //실패 케이스 - 3 - jwt 토큰이 아닌 값이 전달된 경우
        () -> assertThrows(MalformedJwtException.class, () -> jwtTokenParser.parseJwtToken(wrongValueAuthHttpHeader))
    );
  }

}