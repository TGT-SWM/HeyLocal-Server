package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.exception.service.SigninArgumentException;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

class SigninServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private TokenRepository tokenRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private JwtTokenParser jwtTokenParser;
  private SigninService signinService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signinService = new SigninService(userRepository, tokenRepository, passwordEncoder, jwtTokenProvider, jwtTokenParser);
  }

  @Test
  @DisplayName("로그인")
  void signinTest() throws SigninArgumentException {
    //GIVEN
    String rightAccountId = "testAccountId1";
    String rightRawPassword = "testPassword123!";
    String wrongAccountId = "testAccountId2";
    String wrongRawPassword = "testPassword";
    long existId = 3L;
    String existEncodedPassword = "encodedPassword123!";
    String existPhoneNumber = "010-1234-1234";
    String existNickname = "testNickname";
    String accessTokenValue = "accessTokenValue";
    String refreshTokenValue = "refreshTokenValue";
    LocalDateTime accessExpiration = LocalDateTime.now().plusHours(2);
    LocalDateTime refreshExpiration = LocalDateTime.now().plusWeeks(2);

    User userFoundByAccountId = User.builder()
        .id(existId)
        .accountId(rightAccountId)
        .password(existEncodedPassword)
        .userRole(UserRole.TRAVELER)
        .nickname(existNickname)
        .build();

    SigninRequest succeedRequest = SigninRequest.builder()
        .accountId(rightAccountId)
        .password(rightRawPassword)
        .build();
    SigninRequest wrongAccountIdRequest = SigninRequest.builder()
        .accountId(wrongAccountId)
        .password(rightRawPassword)
        .build();
    SigninRequest wrongPasswordRequest = SigninRequest.builder()
        .accountId(rightAccountId)
        .password(wrongRawPassword)
        .build();
    SigninRequest wrongBothRequest = SigninRequest.builder()
        .accountId(wrongAccountId)
        .password(wrongRawPassword)
        .build();

    //Mock 행동 정의 - travelerRepository
    willReturn(Optional.of(userFoundByAccountId)).given(userRepository).findByAccountId(eq(rightAccountId));
    willReturn(Optional.empty()).given(userRepository).findByAccountId(not(eq(rightAccountId)));

    //Mock 행동 정의 - passwordEncoder
    willReturn(true).given(passwordEncoder).matches(eq(rightRawPassword), eq(existEncodedPassword));

    //Mock 행동 정의 - jwtTokenProvider
    willReturn(accessTokenValue).given(jwtTokenProvider).createAccessToken(existId);
    willReturn(refreshTokenValue).given(jwtTokenProvider).createRefreshToken(existId);

    //Mock 행동 정의 - jwtTokenParser
    willReturn(accessExpiration).given(jwtTokenParser).extractExpiration(eq(accessTokenValue));
    willReturn(refreshExpiration).given(jwtTokenParser).extractExpiration(eq(refreshTokenValue));

    //WHEN
    SigninResponse succeedResponse = signinService.signin(succeedRequest);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertEquals(existId, succeedResponse.getId()),
        () -> assertEquals(rightAccountId, succeedResponse.getAccountId()),
        () -> assertEquals(existNickname, succeedResponse.getNickname()),
        () -> assertSame(UserRole.TRAVELER, succeedResponse.getUserRole()),
        () -> assertEquals(accessTokenValue, succeedResponse.getAccessToken()),
        () -> assertEquals(refreshTokenValue, succeedResponse.getRefreshToken()),
        //실패 케이스 - 1 - 존재하지 않는 계정 id
        () -> assertThrows(SigninArgumentException.class, () -> signinService.signin(wrongAccountIdRequest)),
        //실패 케이스 - 2 - 존재하지 않는 password
        () -> assertThrows(SigninArgumentException.class, () -> signinService.signin(wrongPasswordRequest)),
        //실패 케이스 - 3 - 계정 id, password 모두 존재하지 않는 경우
        () -> assertThrows(SigninArgumentException.class, () -> signinService.signin(wrongBothRequest))
    );

  }
}