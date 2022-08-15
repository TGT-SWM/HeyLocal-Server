package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.token.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Import(TokenRepository.class)
@DataJpaTest
class TokenRepositoryTest {
  @Autowired
  private TokenRepository tokenRepository;

  @Test
  @DisplayName("토큰 쌍 저장")
  void saveTokenPairTest() {
    //GIVEN
    String accessValue = "Access Token Value";
    LocalDateTime accessExpired = LocalDateTime.now().plusHours(1);
    String refreshValue = "Access Token Value";
    LocalDateTime refreshExpired = LocalDateTime.now().plusWeeks(2);

    //WHEN
    RefreshToken refreshToken = tokenRepository.saveTokenPair(accessValue, accessExpired, refreshValue, refreshExpired);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertDoesNotThrow(() -> tokenRepository.saveTokenPair(accessValue, accessExpired, refreshValue, refreshExpired)),
        () -> assertEquals(refreshValue, refreshToken.getTokenValue()),
        () -> assertEquals(accessValue, refreshToken.getAccessToken().getTokenValue())
    );

  }
}