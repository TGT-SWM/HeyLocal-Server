package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.repository.TravelerProfileRepository;
import com.heylocal.traveler.repository.TravelerRepository;
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
  private TravelerRepository travelerRepository;
  @Mock
  private TravelerProfileRepository travelerProfileRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  private SignupService signupService;

  @BeforeEach
  void setUp() {
    //MockitoAnnotations.initMocks(this); //deprecated
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signupService = new SignupService(userRepository, travelerRepository, travelerProfileRepository, passwordEncoder);
  }

  @Test
  @DisplayName("아이디 중복 확인")
  void checkAccountIdExistTest() {
    //GIVEN
    String newAccountId = "newAccountId";
    String existAccountId = "testAccountId";

    String password = "testPassword123!";
    String phoneNumber = "010-1234-1234";
    UserType userType = UserType.TRAVELER;
    User existUser = User.builder()
        .accountId(existAccountId)
        .password(password)
        .phoneNumber(phoneNumber)
        .userType(userType)
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
  @DisplayName("전화번호 중복 확인")
  void checkPhoneNumberExistTest() {
    //GIVEN
    String newPhoneNumber = "010-2222-2222";
    String existPhoneNumber = "010-1111-1111";

    String accountId = "testAccountId";
    String password = "testPassword123!";
    UserType userType = UserType.TRAVELER;
    User existUser = User.builder()
        .accountId(accountId)
        .password(password)
        .phoneNumber(existPhoneNumber)
        .userType(userType)
        .build();

    //Mock 행동 정의
    willReturn(Optional.empty()).given(userRepository).findByPhoneNumber(eq(newPhoneNumber)); //newPhoneNumber 전달 시, Optional.empty() 반환
    willReturn(Optional.of(existUser)).given(userRepository).findByPhoneNumber(eq(existPhoneNumber)); //existPhoneNumber 전달 시, 기존의 user 반환

    //WHEN
    UserInfoCheckResponse notExistResult = signupService.checkPhoneNumberExist(newPhoneNumber);
    UserInfoCheckResponse existResult = signupService.checkPhoneNumberExist(existPhoneNumber);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하지 않는 Phone Number 인 경우
        () -> assertFalse(notExistResult.isAlreadyExist()),
        //실패 케이스 - 1 - 존재하는 Phone Number 인 경우
        () -> assertTrue(existResult.isAlreadyExist())
    );
  }

  @Test
  @DisplayName("여행자 회원가입")
  void signupTravelerTest() {
    //GIVEN
    String encodedPassword = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String accountId = "testAccountId";
    String rawPassword = "testPassword123!";
    String nickname = "testNickname";
    String phoneNumber = "010-1111-1111";
    SignupRequest request = SignupRequest.builder()
        .accountId(accountId)
        .password(rawPassword)
        .nickname(nickname)
        .phoneNumber(phoneNumber)
        .build();

    //Mock 행동 정의
    willReturn(encodedPassword).given(passwordEncoder).encode(eq(rawPassword));

    //WHEN

    //THEN
    //성공 케이스 - 1
    assertDoesNotThrow(() -> signupService.signupTraveler(request));
  }

}