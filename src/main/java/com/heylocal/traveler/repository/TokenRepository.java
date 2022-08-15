package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * Access Token, Refresh Token Repository
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TokenRepository {
  private final EntityManager em;

  /**
   * AccessToken 과 Refresh Token 쌍을 저장하는 메서드
   * @param accessValue Access Token 값
   * @param refreshValue Refresh Token 값
   */
  public RefreshToken saveTokenPair(String accessValue, LocalDateTime accessExpired, String refreshValue, LocalDateTime refreshExpired) {
    RefreshToken refreshToken = RefreshToken.builder()
        .tokenValue(refreshValue)
        .expiredDateTime(refreshExpired)
        .build();
    AccessToken accessToken = AccessToken.builder()
        .tokenValue(accessValue)
        .expiredDateTime(accessExpired)
        .build();
    refreshToken.associateAccessToken(accessToken);

    //Cascade.ALL 옵션 때문에, 부모 엔티티(RefreshToken)이 영속화될 때, 자식 엔티티(Access Token)도 영속화된다.
    em.persist(refreshToken);

    return refreshToken;
  }
}
