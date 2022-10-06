package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.SigninCode;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : SigninService
 * author         : 우태균
 * date           : 2022/08/15
 * description    : 로그인 관련 서비스
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SigninService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenParser jwtTokenParser;

  /**
   * 로그인 메서드
   * @param request 요청 DTO
   * @return 로그인 성공 시, 응답 DTO
   * @throws UnauthorizedException 로그인 실패 시
   */
  @Transactional
  public SigninResponse signin(SigninRequest request) throws UnauthorizedException {
    String accountId = request.getAccountId();
    String rawPassword = request.getPassword();
    User user;
    SigninResponse response;
    String[] tokenAry;

    //계정 id 확인
    user = checkAccountId(accountId);
    //비밀번호 확인
    user = checkPassword(rawPassword, user);
    //Access Token, Refresh Token 발급
    tokenAry = issueTokens(user.getId());

    response = SigninResponse.builder()
        .id(user.getId())
        .accountId(user.getAccountId())
        .nickname(user.getNickname())
        .userRole(UserRole.TRAVELER)
        .accessToken(tokenAry[0])
        .refreshToken(tokenAry[1])
        .build();
    return response;
  }

  /**
   * 계정 ID 확인 메서드
   * @param accountId (client가 요청한) 확인할 계정 ID
   * @return 해당 계정 ID를 가지고 있는 Traveler 엔티티
   * @throws UnauthorizedException 존재하지 않는 계정 ID인 경우
   */
  private User checkAccountId(String accountId) throws UnauthorizedException {
    Optional<User> userByAccountId;

    userByAccountId = userRepository.findByAccountId(accountId);
    if (userByAccountId.isEmpty()) {
      throw new UnauthorizedException(SigninCode.NOT_EXIST_SIGNIN_ACCOUNT_ID);
    }

    return userByAccountId.get();
  }

  /**
   * 비밀번호 확인 메서드
   * @param rawPassword (client가 요청한) 확인할 비밀번호
   * @param userByAccountId 요청받은 계정 ID로 찾은 Traveler 엔티티, 비밀번호를 대조할 엔티티
   * @return 비밀번호가 일치하는 경우 해당 Traveler 엔티티 반환
   * @exception UnauthorizedException 비밀번호가 일치하지 않는 경우
   */
  private User checkPassword(String rawPassword, User userByAccountId) throws UnauthorizedException {
    String encodedPasswordOfFoundTraveler;
    boolean isMatchedPassword;

    encodedPasswordOfFoundTraveler = userByAccountId.getPassword();
    isMatchedPassword = checkPasswordMatch(rawPassword, encodedPasswordOfFoundTraveler);
    if (!isMatchedPassword) {
      throw new UnauthorizedException(SigninCode.WRONG_SIGNIN_PASSWORD);
    }

    return userByAccountId;
  }

  /**
   * '일반 비밀번호'와 '인코딩된 비밀번호'가 서로 일치하는지 비교하는 메서드
   * @param rawPassword 일반 비밀번호
   * @param encodedPassword 인코딩된 비밀번호
   * @return 결과, true:일치, false:불일치
   */
  private boolean checkPasswordMatch(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  /**
   * <pre>
   * Access Token, Refresh Token 발급 메서드
   * </pre>
   * @param id 사용자(여행자)의 pk값
   * @return Access Token, Refresh Token 이 담긴 배열
   */
  private String[] issueTokens(long id) {
    String accessTokenValue;
    String refreshTokenValue;

    //토큰 발급
    accessTokenValue = jwtTokenProvider.createAccessToken(id);
    refreshTokenValue = jwtTokenProvider.createRefreshToken(id);

    //토큰 DB 저장
    saveTokenPairToDb(id, accessTokenValue, refreshTokenValue);

    return new String[]{accessTokenValue, refreshTokenValue};
  }

  /**
   * <pre>
   * DB에 Access·Refresh Token 을 저장하는 메서드
   * </pre>
   * @param userId 관련 사용자 id(pk)
   * @param accessTokenValue Access Token 값
   * @param refreshTokenValue Refresh Token 값
   */
  private void saveTokenPairToDb(long userId, String accessTokenValue, String refreshTokenValue) {
    User user;
    AccessToken accessToken;
    RefreshToken refreshToken;
    LocalDateTime refreshExpiration;
    LocalDateTime accessExpiration;

    accessExpiration = jwtTokenParser.extractExpiration(accessTokenValue);
    refreshExpiration = jwtTokenParser.extractExpiration(refreshTokenValue);
    user = userRepository.findById(userId).orElseThrow(
        () -> new IllegalArgumentException("User 를 찾을 수 없습니다.")
    );
    accessToken = AccessToken.builder()
        .tokenValue(accessTokenValue)
        .expiredDateTime(accessExpiration)
        .build();
    refreshToken = RefreshToken.builder()
        .tokenValue(refreshTokenValue)
        .expiredDateTime(refreshExpiration)
        .build();

    try {
      tokenRepository.removeTokenPairByUserId(userId); //기존 토큰들이 있다면 제거
    } catch (NoResultException e) {
      //ignored (관련 토큰을 가지고 있지 않아도, 문제될 것이 없으므로 무시)
    }

    tokenRepository.saveTokenPair(user, refreshToken, accessToken);
  }
}
