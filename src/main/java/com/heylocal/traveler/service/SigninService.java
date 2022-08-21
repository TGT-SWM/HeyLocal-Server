package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.exception.code.SigninCode;
import com.heylocal.traveler.exception.service.SigninArgumentException;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigninService {

  private final TravelerRepository travelerRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenParser jwtTokenParser;

  /**
   * 로그인 메서드
   * @param request 요청 DTO
   * @return 로그인 성공 시, 응답 DTO
   * @throws IllegalArgumentException 로그인 실패 시
   */
  @Transactional
  public SigninResponse signin(SigninRequest request) throws SigninArgumentException {
    String accountId = request.getAccountId();
    String rawPassword = request.getPassword();
    Traveler traveler;
    SigninResponse response;
    String[] tokenAry;

    //계정 id 확인
    traveler = checkAccountId(accountId);
    //비밀번호 확인
    traveler = checkPassword(rawPassword, traveler);
    //Access Token, Refresh Token 발급
    tokenAry = issueTokens(traveler.getId(), traveler.getAccountId(), traveler.getNickname(), traveler.getPhoneNumber());

    response = SigninResponse.builder()
        .id(traveler.getId())
        .accountId(traveler.getAccountId())
        .nickname(traveler.getNickname())
        .phoneNumber(traveler.getPhoneNumber())
        .userType(UserType.TRAVELER)
        .accessToken(tokenAry[0])
        .refreshToken(tokenAry[1])
        .build();
    return response;
  }

  /**
   * 계정 ID 확인 메서드
   * @param accountId (client가 요청한) 확인할 계정 ID
   * @return 해당 계정 ID를 가지고 있는 Traveler 엔티티
   * @throws IllegalArgumentException 존재하지 않는 계정 ID인 경우
   */
  private Traveler checkAccountId(String accountId) throws SigninArgumentException {
    Optional<Traveler> travelerByAccountId;

    travelerByAccountId = travelerRepository.findByAccountId(accountId);
    if (travelerByAccountId.isEmpty()) {
      throw new SigninArgumentException(SigninCode.NOT_EXIST_SIGNIN_ACCOUNT_ID);
    }

    return travelerByAccountId.get();
  }

  /**
   * 비밀번호 확인 메서드
   * @param rawPassword (client가 요청한) 확인할 비밀번호
   * @param travelerByAccountId 요청받은 계정 ID로 찾은 Traveler 엔티티, 비밀번호를 대조할 엔티티
   * @return 비밀번호가 일치하는 경우 해당 Traveler 엔티티 반환
   * @exception IllegalArgumentException 비밀번호가 일치하지 않는 경우
   */
  private Traveler checkPassword(String rawPassword, Traveler travelerByAccountId) throws SigninArgumentException {
    String encodedPasswordOfFoundTraveler;
    boolean isMatchedPassword;

    encodedPasswordOfFoundTraveler = travelerByAccountId.getPassword();
    isMatchedPassword = checkPasswordMatch(rawPassword, encodedPasswordOfFoundTraveler);
    if (!isMatchedPassword) {
      throw new SigninArgumentException(SigninCode.WRONG_SIGNIN_PASSWORD);
    }

    return travelerByAccountId;
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
   * @param accountId 사용자(여행자)의 계정 id
   * @param nickname 사용자(여행자)의 닉네임
   * @param phoneNumber 사용자(여행자)의 휴대폰 번호
   * @return Access Token, Refresh Token 이 담긴 배열
   */
  private String[] issueTokens(long id, String accountId, String nickname, String phoneNumber) {
    String accessTokenValue;
    String refreshTokenValue;

    //토큰 발급
    accessTokenValue = jwtTokenProvider.createAccessToken(id);
    refreshTokenValue = jwtTokenProvider.createRefreshToken(id);

    //토큰 DB 저장
    saveTokenPairToDb(accessTokenValue, refreshTokenValue);

    return new String[]{accessTokenValue, refreshTokenValue};
  }

  /**
   * <pre>
   * DB에 Access·Refresh Token 을 저장하는 메서드
   * </pre>
   * @param accessTokenValue Access Token 값
   * @param refreshTokenValue Refresh Token 값
   */
  private void saveTokenPairToDb(String accessTokenValue, String refreshTokenValue) {
    LocalDateTime refreshExpiration;
    LocalDateTime accessExpiration;

    accessExpiration = jwtTokenParser.extractExpiration(accessTokenValue);
    refreshExpiration = jwtTokenParser.extractExpiration(refreshTokenValue);
    tokenRepository.saveTokenPair(accessTokenValue, accessExpiration, refreshTokenValue, refreshExpiration);
  }
}
