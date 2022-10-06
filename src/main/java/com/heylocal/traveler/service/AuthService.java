package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.TokenException;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.AuthCode;
import com.heylocal.traveler.exception.code.TokenCode;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairRequest;
import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairResponse;

/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : AuthService
 * author         : 우태균
 * date           : 2022/08/18
 * description    : 인증 관련 서비스
 */

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtTokenParser jwtTokenParser;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * id값(pk)으로 User를 조회하는 메서드
   * @param userId id값 (pk)
   * @return 로그인된 유저의 정보
   * @throws TokenException
   */
  @Transactional(readOnly = true)
  public LoginUser findLoginUser(long userId) throws TokenException {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new TokenException(TokenCode.NOT_EXIST_TOKEN_USER_ID)
    );

    return LoginUser.builder()
        .id(user.getId())
        .build();
  }

  /**
   * <pre>
   * Access Token 과 Refresh Token 재발급 메서드
   * </pre>
   * @param request 만료된 Access Token, 만료 안된 Refresh Token
   * @return
   * @throws UnauthorizedException 비정상적인 요청인 경우
   */
  @Transactional
  public TokenPairResponse reissueTokenPair(TokenPairRequest request) throws UnauthorizedException {
    RefreshToken storedRefreshToken;
    AccessToken storedAccessToken;
    long userId = 0L;

    //Refresh Token 검증
    storedRefreshToken = validateRefreshToken(request);

    //Refresh Token 으로부터 userPk 추출
    userId = getUserIdFromRefreshToken(storedRefreshToken);

    //request 받은 AccessToken과 DB에 저장된 AccessToken의 값이 같은지 확인
    storedAccessToken = isSameAccessTokenValue(storedRefreshToken.getAccessToken(), request.getAccessToken());

    //조회한 AccessToken이 정말 만료되었는지 확인
    validateExpiredAccessToken(storedAccessToken);

    //Access Token 과 Refresh Token 발급
    String newAccessTokenValue = jwtTokenProvider.createAccessToken(userId);
    String newRefreshTokenValue = jwtTokenProvider.createRefreshToken(userId);

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
   * @throws UnauthorizedException 검증 실패시
   */
  private RefreshToken validateRefreshToken(TokenPairRequest request) throws UnauthorizedException {
    RefreshToken storedRefreshToken;

    //Refresh Token 조회
    storedRefreshToken = tokenRepository.findRefreshTokenByValue(request.getRefreshToken()).orElseThrow(
        () -> new UnauthorizedException(AuthCode.NOT_EXIST_REFRESH_TOKEN)
    );

    //Refresh Token 이 만료되었는지 확인
    try {
      jwtTokenParser.parseJwtToken(storedRefreshToken.getTokenValue());
    } catch (ExpiredJwtException expiredJwtException) {
      throw new UnauthorizedException(AuthCode.EXPIRED_REFRESH_TOKEN);
    }

    return storedRefreshToken;
  }

  /**
   * Refresh Token 에 담긴 사용자 id(pk)값을 추출하는 메서드
   * @param refreshToken 추출할 토큰 엔티티
   * @return 추출한 사용자 id(pk)
   * @throws UnauthorizedException 존재하지 않는 토큰 값일 경우
   */
  private long getUserIdFromRefreshToken(RefreshToken refreshToken) throws UnauthorizedException {
    Claims claims = jwtTokenParser.parseJwtToken(refreshToken.getTokenValue())
        .orElseThrow(
            () -> new UnauthorizedException(AuthCode.NOT_EXIST_REFRESH_TOKEN)
        );

    return claims.get("userPk", Long.class);
  }

  /**
   * 같은 Access Token 값 인지 확인하는 메서드
   * @param storedAccessToken 확인할 Access Token
   * @param requestAccessToken 요청받은 Access Token
   * @return
   * @throws UnauthorizedException
   */
  private AccessToken isSameAccessTokenValue(AccessToken storedAccessToken, String requestAccessToken) throws UnauthorizedException {
    if (!storedAccessToken.getTokenValue().equals(requestAccessToken)) {
      tokenRepository.removeTokenPairByAccessValue(storedAccessToken.getTokenValue()); //비정상 접근이므로, 모든 토큰 쌍 제거
      throw new UnauthorizedException(AuthCode.NOT_MATCH_PAIR);
    }
    return storedAccessToken;
  }

  /**
   * 정말로 만료된 Access Token인지 확인하는 메서드
   * @param accessToken 확인할 access token
   * @return 만료된 Access Token 의 클레임에 바인딩된 유저 id(pk)
   * @throws UnauthorizedException 검증 실패 시
   */
  private void validateExpiredAccessToken(AccessToken accessToken) throws UnauthorizedException {
    boolean isExpiredAccessToken = false;

    try {
      jwtTokenParser.parseJwtToken(accessToken.getTokenValue());
    } catch (ExpiredJwtException expiredJwtException) {
      isExpiredAccessToken = true;
    }
    if (!isExpiredAccessToken) {
      tokenRepository.removeTokenPairByAccessValue(accessToken.getTokenValue()); //비정상 접근이므로, 모든 토큰 쌍 제거
      throw new UnauthorizedException(AuthCode.NOT_EXPIRED_ACCESS_TOKEN);
    }
  }
}
