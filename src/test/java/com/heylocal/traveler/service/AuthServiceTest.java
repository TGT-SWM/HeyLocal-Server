package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.code.AuthCode;
import com.heylocal.traveler.exception.service.AuthException;
import com.heylocal.traveler.exception.service.TokenException;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairRequest;
import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class AuthServiceTest {
  String secretKey = "H1jo5zWVaJiCiD6B/gpcdD0IspmEBDQ4Q0kxxUTkgU1ea97KF3hblwEHonHG6sy4KsLcv4u6IpfVI5WHcpg4unzhOZExuWCFTiVY2HQK5dnip2YPlCwPs4PeNAw/k2o6vJZvGn5HZ8whEOgqAtCvauxY8rIMptC9QUbh18B8bT5fZDEs5A5NzXx7YEUQuB3+TSI2xhpytDo6bIN3kSn/veuOdEc7DThkQ5xDZw==%";
  @Mock
  private UserRepository userRepository;
  @Mock
  private TokenRepository tokenRepository;
  @Mock
  private JwtTokenParser jwtTokenParser;
  @Mock
  private JwtTokenProvider jwtTokenProvider;
  private AuthService authService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    authService = new AuthService(userRepository, tokenRepository, jwtTokenParser, jwtTokenProvider);
  }

  @Test
  @DisplayName("id값으로 Traveler 조회")
  void findLoginTravelerTest() throws TokenException {
    //GIVEN
    long userId = 1L;
    long notExistId = 3L;
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    String phoneNumber = "010-1111-1111";
    User user = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .id(userId)
        .build();

    //Mock 행동 정의
    willReturn(Optional.of(user)).given(userRepository).findById(userId);
    willReturn(Optional.empty()).given(userRepository).findById(notExistId);

    //WHEN
    LoginUser loginTraveler = authService.findLoginUser(userId);

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> authService.findLoginUser(userId)),
        () -> assertEquals(user.getId(), loginTraveler.getId()),
        () -> assertEquals(user.getAccountId(), loginTraveler.getAccountId()),
        () -> assertEquals(user.getNickname(), loginTraveler.getNickname()),
        () -> assertEquals(UserRole.TRAVELER, loginTraveler.getUserRole()),
        //실패 케이스 - 1 - 존재하지 않는 pk일 때
        () -> assertThrows(TokenException.class, () -> authService.findLoginUser(notExistId))
    );
  }

  @Test
  @DisplayName("토큰 쌍 재발급 - 재발급 성공")
  void reissueTokenPairTestSucceed() throws AuthException {
    //GIVEN
    long userPk = 1L;

    //만료된 Access Token
    String expiredAccessTokenValue = "expiredAccessTokenValue";
    AccessToken expiredAccessToken = AccessToken.builder()
        .id(10)
        .tokenValue(expiredAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().minusHours(2))
        .build();

    //만료되지 않은 Refresh Token
    String notExpiredRefreshTokenValue = "notExpiredRefreshTokenValue";
    RefreshToken notExpiredRefreshToken = RefreshToken.builder()
        .id(11)
        .tokenValue(notExpiredRefreshTokenValue)
        .expiredDateTime(LocalDateTime.now().plusWeeks(2))
        .build();

    notExpiredRefreshToken.associateAccessToken(expiredAccessToken);

    TokenPairRequest succeedReissueRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();

    String reissuedAccessToken = "reissuedAccessToken";
    String reissuedRefreshToken = "reissuedRefreshToken";

    //Mock 행동 정의 - JwtTokenProvider
    willReturn(reissuedAccessToken).given(jwtTokenProvider).createAccessToken(userPk);
    willReturn(reissuedRefreshToken).given(jwtTokenProvider).createRefreshToken(userPk);

    //Mock 행동 정의 - JwtTokenParser.parseJwtToken
    Claims claimValue = new DefaultClaims();
    claimValue.put("userPk", userPk);
    willReturn(Optional.of(claimValue)).given(jwtTokenParser).parseJwtToken(eq(notExpiredRefreshTokenValue));
    willThrow(ExpiredJwtException.class).given(jwtTokenParser).parseJwtToken(eq(expiredAccessTokenValue));


    //Mock 행동 정의 - TokenRepository.findRefreshTokenByValue
    willReturn(Optional.of(notExpiredRefreshToken)).given(tokenRepository).findRefreshTokenByValue(eq(notExpiredRefreshTokenValue));

    //Mock 행동 정의 - TokenRepository.findAccessTokenByValue
    willReturn(Optional.of(expiredAccessToken)).given(tokenRepository).findAccessTokenByValue(eq(expiredAccessTokenValue));

    //WHEN
    TokenPairResponse response = authService.reissueTokenPair(succeedReissueRequest);

    //THEN
    assertAll(
        () -> assertEquals(reissuedAccessToken, response.getAccessToken()),
        () -> assertEquals(reissuedRefreshToken, response.getRefreshToken())
    );
  }

  @Test
  @DisplayName("토큰 쌍 재발급 - 재발급 실패 - Refresh Token이 존재하지 않는 경우")
  void reissueTokenPairTestFailNotExistRefreshToken() {
    //GIVEN
    long userPk = 1L;

    //존재하는 Access Token
    String existAccessTokenValue = "existAccessTokenValue";

    //존재하지 않는 Refresh Token Value
    String notExistRefreshTokenValue = "notExistRefreshTokenValue";

    TokenPairRequest failReissueRequest = TokenPairRequest.builder()
        .accessToken(existAccessTokenValue)
        .refreshToken(notExistRefreshTokenValue)
        .build();

    //Mock 행동 정의 - TokenRepository.findRefreshTokenByValue
    willReturn(Optional.empty()).given(tokenRepository).findRefreshTokenByValue(eq(notExistRefreshTokenValue));

    //WHEN

    //THEN
    assertThrows(AuthException.class,
        () -> authService.reissueTokenPair(failReissueRequest),
        AuthCode.NOT_EXIST_REFRESH_TOKEN.getDescription()
    );
  }

  @Test
  @DisplayName("토큰 쌍 재발급 - 재발급 실패 - Access·Refresh Token이 매치되지 않는 경우")
  void reissueTokenPairTestFailNotMatchedToken() {
    //GIVEN
    long userPk = 1L;

    //notExpiredRefreshTokenValue 와 매치되는 만료된 Access Token
    String expiredAccessTokenValue = "expiredAccessTokenValue";
    AccessToken expiredAccessToken = AccessToken.builder()
        .id(10)
        .tokenValue(expiredAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().minusHours(2))
        .build();
    //notExpiredRefreshTokenValue 와 매치되지 않는 Access Token
    String notMatchedAccessTokenValue = "notMatchedAccessTokenValue";
    AccessToken notMatchedAccessToken = AccessToken.builder()
        .id(14)
        .tokenValue(notMatchedAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().plusHours(2))
        .build();
    //만료되지 않은 Refresh Token
    String notExpiredRefreshTokenValue = "notExpiredRefreshTokenValue";
    RefreshToken notExpiredRefreshToken = RefreshToken.builder()
        .id(11)
        .tokenValue(notExpiredRefreshTokenValue)
        .expiredDateTime(LocalDateTime.now().plusWeeks(2))
        .build();

    notExpiredRefreshToken.associateAccessToken(expiredAccessToken);

    TokenPairRequest failReissueRequest = TokenPairRequest.builder()
        .accessToken(notMatchedAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();

    //Mock 행동 정의 - JwtTokenParser.parseJwtToken
    Claims claimValue = new DefaultClaims();
    claimValue.put("userPk", userPk);
    willReturn(Optional.of(claimValue)).given(jwtTokenParser).parseJwtToken(eq(notExpiredRefreshTokenValue));


    //Mock 행동 정의 - TokenRepository.findRefreshTokenByValue
    willReturn(Optional.of(notExpiredRefreshToken)).given(tokenRepository).findRefreshTokenByValue(eq(notExpiredRefreshTokenValue));

    //WHEN

    //THEN
    assertThrows(AuthException.class,
        () -> authService.reissueTokenPair(failReissueRequest),
        AuthCode.NOT_MATCH_PAIR.getDescription()
    );
  }

  @Test
  @DisplayName("토큰 쌍 재발급 - 재발급 실패 - Access Token이 아직 만료되지 않는 경우")
  void reissueTokenPairTestFailNotExpiredAccessToken() {
    //GIVEN
    long userPk = 1L;

    //만료되지 않은 Access Token
    String notExpiredAccessTokenValue = "notExpiredAccessTokenValue";
    AccessToken notExpiredAccessToken = AccessToken.builder()
        .id(12)
        .tokenValue(notExpiredAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().plusHours(2))
        .build();
    //만료되지 않은 Refresh Token
    String notExpiredRefreshTokenValue = "notExpiredRefreshTokenValue";
    RefreshToken notExpiredRefreshToken = RefreshToken.builder()
        .id(11)
        .tokenValue(notExpiredRefreshTokenValue)
        .expiredDateTime(LocalDateTime.now().plusWeeks(2))
        .build();

    notExpiredRefreshToken.associateAccessToken(notExpiredAccessToken);

    TokenPairRequest failReissueRequest = TokenPairRequest.builder()
        .accessToken(notExpiredAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();

    //Mock 행동 정의 - JwtTokenParser.parseJwtToken
    Claims claimValue = new DefaultClaims();
    claimValue.put("userPk", userPk);
    willReturn(Optional.of(claimValue)).given(jwtTokenParser).parseJwtToken(eq(notExpiredRefreshTokenValue));
    willReturn(Optional.of(claimValue)).given(jwtTokenParser).parseJwtToken(eq(notExpiredAccessTokenValue));


    //Mock 행동 정의 - TokenRepository.findRefreshTokenByValue
    willReturn(Optional.of(notExpiredRefreshToken)).given(tokenRepository).findRefreshTokenByValue(eq(notExpiredRefreshTokenValue));

    //WHEN

    //THEN
    assertThrows(AuthException.class,
        () -> authService.reissueTokenPair(failReissueRequest),
        AuthCode.NOT_EXPIRED_ACCESS_TOKEN.getDescription()
    );
  }

  @Test
  @DisplayName("토큰 쌍 재발급 - 재발급 실패 - Refresh Token이 만료된 경우")
  void reissueTokenPairTestFailExpiredRefreshToken() {
    //GIVEN
    long userPk = 1L;

    //만료된 Access Token
    String expiredAccessTokenValue = "expiredAccessTokenValue";
    AccessToken expiredAccessToken = AccessToken.builder()
        .id(10)
        .tokenValue(expiredAccessTokenValue)
        .expiredDateTime(LocalDateTime.now().minusHours(2))
        .build();
    //만료된 Refresh Token
    String expiredRefreshTokenValue = "expiredRefreshTokenValue";
    RefreshToken expiredRefreshToken = RefreshToken.builder()
        .id(13)
        .tokenValue(expiredRefreshTokenValue)
        .expiredDateTime(LocalDateTime.now().minusHours(5))
        .build();

    expiredRefreshToken.associateAccessToken(expiredAccessToken);

    TokenPairRequest failReissueRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(expiredRefreshTokenValue)
        .build();

    //Mock 행동 정의 - JwtTokenParser.parseJwtToken
    willThrow(ExpiredJwtException.class).given(jwtTokenParser).parseJwtToken(expiredRefreshTokenValue);


    //Mock 행동 정의 - TokenRepository.findRefreshTokenByValue
    willReturn(Optional.of(expiredRefreshToken)).given(tokenRepository).findRefreshTokenByValue(eq(expiredRefreshTokenValue));

    //WHEN

    //THEN
    assertThrows(AuthException.class,
        () -> authService.reissueTokenPair(failReissueRequest),
        AuthCode.EXPIRED_REFRESH_TOKEN.getDescription()
    );
  }
}