package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.service.TokenException;
import com.heylocal.traveler.repository.TokenRepository;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import com.heylocal.traveler.util.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class AuthServiceTest {
  @Mock
  private TravelerRepository travelerRepository;
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
    authService = new AuthService(travelerRepository, tokenRepository, jwtTokenParser, jwtTokenProvider);
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
    Traveler traveler = Traveler.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .phoneNumber(phoneNumber)
        .userType(UserType.TRAVELER)
        .id(userId)
        .build();

    //Mock 행동 정의
    willReturn(Optional.of(traveler)).given(travelerRepository).findById(userId);
    willReturn(Optional.empty()).given(travelerRepository).findById(notExistId);

    //WHEN
    LoginUser loginTraveler = authService.findLoginTraveler(userId);

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> authService.findLoginTraveler(userId)),
        () -> assertEquals(traveler.getId(), loginTraveler.getId()),
        () -> assertEquals(traveler.getAccountId(), loginTraveler.getAccountId()),
        () -> assertEquals(traveler.getNickname(), loginTraveler.getNickname()),
        () -> assertEquals(traveler.getPhoneNumber(), loginTraveler.getPhoneNumber()),
        () -> assertEquals(UserType.TRAVELER, loginTraveler.getUserType()),
        //실패 케이스 - 1 - 존재하지 않는 pk일 때
        () -> assertThrows(TokenException.class, () -> authService.findLoginTraveler(notExistId))
    );
  }

  //TODO - reissueTokenPair
}