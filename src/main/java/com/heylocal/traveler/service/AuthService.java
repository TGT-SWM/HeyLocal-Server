package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.code.AuthCode;
import com.heylocal.traveler.exception.code.TokenCode;
import com.heylocal.traveler.exception.service.AuthException;
import com.heylocal.traveler.exception.service.TokenException;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.AuthTokenDto.*;

/**
 * 인가 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {
  private final TravelerRepository travelerRepository;
  private final TokenRepository tokenRepository;
  private final JwtTokenParser jwtTokenParser;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * id값(pk)으로 Traveler를 조회하는 메서드
   * @param travelerId id값 (pk)
   * @return 로그인된 유저의 정보
   */
  @Transactional
  public LoginUser findLoginTraveler(long travelerId) throws TokenException {
    Traveler traveler = travelerRepository.findById(travelerId).orElseThrow(
        () -> new TokenException(TokenCode.NOT_EXIST_TOKEN_USER_ID)
    );

    return LoginUser.builder()
        .id(traveler.getId())
        .accountId(traveler.getAccountId())
        .nickname(traveler.getNickname())
        .phoneNumber(traveler.getPhoneNumber())
        .userType(UserType.TRAVELER)
        .build();
  }

  /**
   * <pre>
   * Access Token 과 Refresh Token 재발급 메서드
   * </pre>
   * @param request 만료된 Access Token, 만료 안된 Refresh Token
   * @return
   * @throws AuthException 비정상적인 요청인 경우
   */
  @Transactional
  public TokenPairResponse reissueTokenPair(TokenPairRequest request) throws AuthException {
    RefreshToken storedRefreshToken;
    AccessToken storedAccessToken;
    long userId = 0L;

    //Refresh Token 검증
    storedRefreshToken = validateRefreshToken(request);

    //request 받은 AccessToken과 DB에 저장된 AccessToken의 값이 같은지 확인
    storedAccessToken = isSameAccessTokenValue(storedRefreshToken.getAccessToken(), request.getAccessToken());

    //조회한 AccessToken이 정말 만료되었는지 확인 및 user id(pk) 추출
    userId = validateExpiredAccessToken(storedAccessToken);

    //Access Token 과 Refresh Token 발급
    String newAccessTokenValue = jwtTokenProvider.createAccessToken(userId);
    String newRefreshTokenValue = jwtTokenProvider.createRefreshToken();

    //새로운 Access Token, Refresh Token 으로 업데이트
    storedAccessToken.updateTokenValue(newAccessTokenValue);
    storedRefreshToken.updateTokenValue(newRefreshTokenValue);

    return TokenPairResponse.builder()
        .accessToken(newAccessTokenValue)
        .refreshToken(newRefreshTokenValue)
        .build();
  }

  /**
   * <pre>
   * Refresh Token 검증
   * - 실제로 존재하는 Refresh Token 인지
   * - 만료된 Refresh Token 인지
   * </pre>
   * @param request
   * @return DB에 저장된 Refresh Token 엔티티
   * @throws AuthException 검증 실패시
   */
  private RefreshToken validateRefreshToken(TokenPairRequest request) throws AuthException {
    RefreshToken storedRefreshToken;

    //Refresh Token 조회
    storedRefreshToken = tokenRepository.findRefreshTokenByValue(request.getRefreshToken()).orElseThrow(
        () -> new AuthException(AuthCode.NOT_EXIST_REFRESH_TOKEN)
    );

    //Refresh Token 이 만료되었는지 확인
    try {
      jwtTokenParser.parseJwtToken(storedRefreshToken.getTokenValue());
    } catch (ExpiredJwtException expiredJwtException) {
      throw new AuthException(AuthCode.EXPIRED_REFRESH_TOKEN);
    }

    return storedRefreshToken;
  }

  /**
   * 같은 Access Token 값 인지 확인하는 메서드
   * @param storedAccessToken 확인할 Access Token
   * @param requestAccessToken 요청받은 Access Token
   * @return
   * @throws AuthException
   */
  private AccessToken isSameAccessTokenValue(AccessToken storedAccessToken, String requestAccessToken) throws AuthException {
    if (!storedAccessToken.getTokenValue().equals(requestAccessToken)) {
      tokenRepository.removeTokenPairByAccessValue(storedAccessToken.getTokenValue()); //비정상 접근이므로, 모든 토큰 쌍 제거
      throw new AuthException(AuthCode.NOT_MATCH_PAIR);
    }
    return storedAccessToken;
  }

  /**
   * 정말로 만료된 Access Token인지 확인하는 메서드
   * @param accessToken 확인할 access token
   * @return 만료된 Access Token 의 클레임에 바인딩된 유저 id(pk)
   * @throws AuthException 검증 실패 시
   */
  private long validateExpiredAccessToken(AccessToken accessToken) throws AuthException {
    boolean isExpiredAccessToken = false;
    long userId = 0;

    try {
      Claims claims = jwtTokenParser.parseJwtToken(accessToken.getTokenValue()).get();
      userId = claims.get("userPk", Long.class);
    } catch (ExpiredJwtException expiredJwtException) {
      isExpiredAccessToken = true;
    }
    if (!isExpiredAccessToken) {
      tokenRepository.removeTokenPairByAccessValue(accessToken.getTokenValue()); //비정상 접근이므로, 모든 토큰 쌍 제거
      throw new AuthException(AuthCode.NOT_EXPIRED_ACCESS_TOKEN);
    }

    return userId;
  }
}
