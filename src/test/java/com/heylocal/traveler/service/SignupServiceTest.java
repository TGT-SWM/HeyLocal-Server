package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.repository.UserProfileRepository;
import com.heylocal.traveler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.heylocal.traveler.dto.SignupDto.SignupRequest;
import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.willReturn;

class SignupServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserProfileRepository userProfileRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  private SignupService signupService;

  @BeforeEach
  void setUp() {
    //MockitoAnnotations.initMocks(this); //deprecated
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signupService = new SignupService(userRepository, userProfileRepository, passwordEncoder);
  }

  @Test
  @DisplayName("아이디 중복 확인")
  void checkAccountIdExistTest() {
    //GIVEN
    String newAccountId = "newAccountId";
    String existAccountId = "testAccountId";

    String password = "testPassword123!";
    UserRole userRole = UserRole.TRAVELER;
    User existUser = User.builder()
        .accountId(existAccountId)
        .password(password)
        .userRole(userRole)
        .build();

    //Mock 행동 정의
    willReturn(Optional.empty()).given(userRepository).findByAccountId(eq(newAccountId)); //newAccountId 전달 시, Optional.empty() 반환
    willReturn(Optional.of(existUser)).given(userRepository).findByAccountId(eq(existAccountId)); //existAccountId 전달 시, 기존의 user 반환

    //WHEN
    UserInfoCheckResponse notExistResult = signupService.checkAccountIdExist(newAccountId);
    UserInfoCheckResponse existResult = signupService.checkAccountIdExist(existAccountId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하지 않는 account id 인 경우
        () -> assertFalse(notExistResult.isAlreadyExist()),
        //실패 케이스 - 1 - 존재하는 account id 인 경우
        () -> assertTrue(existResult.isAlreadyExist())
    );
  }

  @Test
  @DisplayName("사용자 회원가입")
  void signupUserTest() {
    //GIVEN
    long userId = 3;
    String encodedPassword = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String accountId = "testAccountId";
    String rawPassword = "testPassword123!";
    String nickname = "testNickname";
    SignupRequest request = SignupRequest.builder()
        .accountId(accountId)
        .password(rawPassword)
        .nickname(nickname)
        .build();
    User user = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPassword)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .build();

    //Mock 행동 정의
    willReturn(encodedPassword).given(passwordEncoder).encode(eq(rawPassword));
    willReturn(user).given(userRepository).saveUser(eq(accountId), eq(encodedPassword), eq(nickname), eq(UserRole.TRAVELER));

    //WHEN

    //THEN
    //성공 케이스 - 1
    assertDoesNotThrow(() -> signupService.signupUser(request));
  }

}