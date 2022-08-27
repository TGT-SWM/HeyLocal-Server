package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
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
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    String accessValue = "Access Token Value";
    LocalDateTime accessExpired = LocalDateTime.now().plusHours(1);
    String refreshValue = "Access Token Value";
    LocalDateTime refreshExpired = LocalDateTime.now().plusWeeks(2);
    User user = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .build();
    AccessToken accessToken = AccessToken.builder()
            .tokenValue(accessValue)
            .expiredDateTime(accessExpired)
            .build();
    RefreshToken refreshToken = RefreshToken.builder()
            .tokenValue(refreshValue)
            .expiredDateTime(refreshExpired)
            .build();

    em.persist(user);

    //WHEN
    refreshToken = tokenRepository.saveTokenPair(user, refreshToken, accessToken);

    //THEN
    assertEquals(accessToken, refreshToken.getAccessToken());
    assertEquals(user, refreshToken.getUser());
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
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    User user = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .build();

    em.persist(user);
    storedRefreshToken.associateAccessToken(storedAccessToken);
    user.registerAccessToken(storedAccessToken);
    user.registerRefreshToken(storedRefreshToken);
    em.persist(storedRefreshToken);

    return storedRefreshToken;
  }

  @Test
  @DisplayName("사용자 id(pk)로 토큰쌍 삭제")
  void removeTokenPairByUserIdTest() {
    //GIVEN
    long userId;
    String storedUserAccountId = "testAccountId";
    String storedUserNickname = "testNickname";
    String storedUserPassword = "encodedPassword123!";
    User storedUser = User.builder()
        .accountId(storedUserAccountId)
        .nickname(storedUserNickname)
        .password(storedUserPassword)
        .userRole(UserRole.TRAVELER)
        .build();
    String storedAccessTokenValue = "validAccessTokenValue";
    AccessToken storedAccessToken = AccessToken.builder()
        .tokenValue(storedAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().plusHours(2))
        .build();
    String storedRefreshTokenValue = "validRefreshTokenValue";
    RefreshToken storedRefreshToken = RefreshToken.builder()
        .tokenValue(storedRefreshTokenValue)
        .expiredDateTime(LocalDateTime.now().plusWeeks(2))
        .build();

    storedRefreshToken.associateAccessToken(storedAccessToken);
    storedUser.registerRefreshToken(storedRefreshToken);
    storedUser.registerAccessToken(storedAccessToken);

    em.persist(storedUser);
    userId = storedUser.getId();

    //WHEN
    User resultUser = tokenRepository.removeTokenPairByUserId(userId);

    //THEN
    assertAll(
        () -> assertNull(resultUser.getAccessToken()),
        () -> assertNull(resultUser.getRefreshToken())
    );
  }
}