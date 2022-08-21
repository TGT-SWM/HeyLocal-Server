package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(TokenRepository.class)
@DataJpaTest
class TokenRepositoryTest {
  @Autowired
  private TokenRepository tokenRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("토큰 쌍 저장")
  void saveTokenPairTest() {
    //GIVEN
    long userId = 3L;
    String accessValue = "Access Token Value";
    LocalDateTime accessExpired = LocalDateTime.now().plusHours(1);
    String refreshValue = "Access Token Value";
    LocalDateTime refreshExpired = LocalDateTime.now().plusWeeks(2);

    //WHEN
    RefreshToken refreshToken = tokenRepository.saveTokenPair(userId, accessValue, accessExpired, refreshValue, refreshExpired);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertDoesNotThrow(() -> tokenRepository.saveTokenPair(userId, accessValue, accessExpired, refreshValue, refreshExpired)),
        () -> assertEquals(refreshValue, refreshToken.getTokenValue()),
        () -> assertEquals(accessValue, refreshToken.getAccessToken().getTokenValue())
    );

  }

  @Test
  @DisplayName("Refresh Token 값으로 Refresh Token 엔티티 조회")
  void findRefreshTokenByValueTest() {
    //GIVEN
    RefreshToken storedRefreshToken = saveTokenPair();
    AccessToken storedAccessToken = storedRefreshToken.getAccessToken();
    String accessTokenValue = storedAccessToken.getTokenValue();
    String refreshTokenValue = storedRefreshToken.getTokenValue();
    String notExistRefreshTokenValue = "not valid refresh token value";

    //WHEN
    Optional<RefreshToken> succeedResult = tokenRepository.findRefreshTokenByValue(refreshTokenValue);
    Optional<RefreshToken> failResult = tokenRepository.findRefreshTokenByValue(notExistRefreshTokenValue);

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertAll(
            () -> assertTrue(succeedResult.isPresent()),
            () -> assertEquals(storedRefreshToken, succeedResult.get())
        ),
        //실패 케이스 - 1 - 존재하지 않는 refresh value 로 조회
        () -> assertFalse(failResult.isPresent())
    );
  }

  @Test
  @DisplayName("Access Token 값으로 Access Token 엔티티 조회")
  void findAccessTokenByValueTest() {
    //GIVEN
    RefreshToken storedRefreshToken = saveTokenPair();
    AccessToken storedAccessToken = storedRefreshToken.getAccessToken();
    String accessTokenValue = storedAccessToken.getTokenValue();
    String refreshTokenValue = storedRefreshToken.getTokenValue();
    String notExistAccessTokenValue = "not valid access token value";

    //WHEN
    Optional<AccessToken> succeedResult = tokenRepository.findAccessTokenByValue(accessTokenValue);
    Optional<AccessToken> failResult = tokenRepository.findAccessTokenByValue(notExistAccessTokenValue);

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertAll(
            () -> assertTrue(succeedResult.isPresent()),
            () -> assertEquals(storedAccessToken, succeedResult.get())
        ),
        //실패 케이스 - 1 - 존재하지 않는 refresh value 로 조회
        () -> assertFalse(failResult.isPresent())
    );
  }

  @Test
  @DisplayName("Refresh Token 값으로 Refresh·Access Token 모두 삭제")
  void removeTokenPairByRefreshValueTest() {
    //GIVEN
    RefreshToken storedRefreshToken = saveTokenPair();
    AccessToken storedAccessToken = storedRefreshToken.getAccessToken();
    String accessTokenValue = storedAccessToken.getTokenValue();
    String refreshTokenValue = storedRefreshToken.getTokenValue();
    String notExistRefreshTokenValue = "notExistRefreshTokenValue";

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장된 Refresh·Access Token 제거
        () -> assertDoesNotThrow(() -> tokenRepository.removeTokenPairByRefreshValue(refreshTokenValue)),
        //실패 케이스 - 1 - 존재하지 않는 Refresh Token 값인 경우
        () -> assertThrows(IllegalArgumentException.class,
            () -> tokenRepository.removeTokenPairByRefreshValue(notExistRefreshTokenValue),
            "존재하지 않는 Refresh Token 값입니다.")
    );
  }

  @Test
  @DisplayName("Access Token 값으로 Refresh·Access Token 모두 삭제")
  void removeTokenPairByAccessValueTest() {
    //GIVEN
    RefreshToken storedRefreshToken = saveTokenPair();
    AccessToken storedAccessToken = storedRefreshToken.getAccessToken();
    String accessTokenValue = storedAccessToken.getTokenValue();
    String refreshTokenValue = storedRefreshToken.getTokenValue();
    String notExistAccessTokenValue = "notExistAccessTokenValue";

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장된 Access·Refresh Token 제거
        () -> assertDoesNotThrow(() -> tokenRepository.removeTokenPairByAccessValue(accessTokenValue)),
        //실패 케이스 - 1 - 존재하지 않는 Access Token 값인 경우
        () -> assertThrows(IllegalArgumentException.class,
            () -> tokenRepository.removeTokenPairByAccessValue(notExistAccessTokenValue),
            "존재하지 않는 Access Token 값입니다.")
    );
  }

  private RefreshToken saveTokenPair() {
    String accessTokenValue = "accessTokenValue";
    String refreshTokenValue = "refreshTokenValue";
    AccessToken storedAccessToken = AccessToken.builder()
        .tokenValue(accessTokenValue)
        .expiredDateTime(LocalDateTime.now().plusHours(2))
        .build();
    RefreshToken storedRefreshToken = RefreshToken.builder()
        .tokenValue(refreshTokenValue)
        .expiredDateTime(LocalDateTime.now().plusWeeks(2))
        .accessToken(storedAccessToken)
        .build();

    storedRefreshToken.associateAccessToken(storedAccessToken);
    em.persist(storedRefreshToken);

    return storedRefreshToken;
  }
}