package com.heylocal.traveler.util.jwt;

import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.util.date.LocalDateTimeTransformer;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

@Slf4j
class JwtTokenParserTest {

  @Mock
  LocalDateTimeTransformer localDateTimeTransformer;
  String secretKey = "H1jo5zWVaJiCiD6B/gpcdD0IspmEBDQ4Q0kxxUTkgU1ea97KF3hblwEHonHG6sy4KsLcv4u6IpfVI5WHcpg4unzhOZExuWCFTiVY2HQK5dnip2YPlCwPs4PeNAw/k2o6vJZvGn5HZ8whEOgqAtCvauxY8rIMptC9QUbh18B8bT5fZDEs5A5NzXx7YEUQuB3+TSI2xhpytDo6bIN3kSn/veuOdEc7DThkQ5xDZw==%";
  JwtTokenParser jwtTokenParser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.jwtTokenParser = new JwtTokenParser(secretKey, localDateTimeTransformer);
  }

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

    //WHEN
    Optional<Claims> claimsOptional = jwtTokenParser.parseJwtToken(validToken);

    //THEN
    assertAll(
      //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> claimsOptional.get()),
      //성공 케이스 - 2
        () -> assertEquals(claimValue, claimsOptional.get().get(claimName)),
      //실패 케이스 - 1 - 유효기간이 만료된 경우
        () -> assertThrows(ExpiredJwtException.class, () -> jwtTokenParser.parseJwtToken(expiredToken))
    );
  }

  @Test
  @DisplayName("토큰 만료기간 추출")
  void extractExpirationTest() {
    //GIVEN
    long userPk = 3L;
    String accountId = "testAccountId";
    String nickname = "testNickname";
    String phoneNumber = "010-1234-1234";
    UserType userType = UserType.TRAVELER;

    int accessTokenValidMilliSec = 7200000; //2시간
    Date now = new Date();
    Date expectExpiration = new Date(now.getTime() + accessTokenValidMilliSec);
    LocalDateTime expectExpirationLocalDateTime = expectExpiration.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
    String tokenValue = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("http://traveler.heylocal.p-e.kr")
        .setIssuedAt(now)
        .setExpiration(expectExpiration)
        .claim("userPk", userPk)
        .claim("accountId", accountId)
        .claim("nickname", nickname)
        .claim("phoneNumber", phoneNumber)
        .claim("userType", userType)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    //Mock 행동 정의
    willReturn(expectExpirationLocalDateTime).given(localDateTimeTransformer).dateToLocalDateTime(any(Date.class));

    //WHEN
    LocalDateTime expiration = jwtTokenParser.extractExpiration(tokenValue);

    //THEN
    assertEquals(expectExpirationLocalDateTime, expiration);
  }
}