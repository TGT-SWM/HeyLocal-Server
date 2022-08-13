package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.heylocal.traveler.dto.SignupDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  private UserService userService;

  @BeforeEach
  void setUp() {
    //MockitoAnnotations.initMocks(this); //deprecated
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.userService = new UserService(userRepository);
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
    User existUser = new User(existAccountId, password, phoneNumber, userType);

    //Mock 행동 정의
    willReturn(Optional.empty()).given(userRepository).findByAccountId(eq(newAccountId)); //newAccountId 전달 시, Optional.empty() 반환
    willReturn(Optional.of(existUser)).given(userRepository).findByAccountId(eq(existAccountId)); //existAccountId 전달 시, 기존의 user 반환

    //WHEN
    UserInfoCheckResponse notExistResult = userService.checkAccountIdExist(newAccountId);
    UserInfoCheckResponse existResult = userService.checkAccountIdExist(existAccountId);

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
    User existUser = new User(accountId, password, existPhoneNumber, userType);

    //Mock 행동 정의
    willReturn(Optional.empty()).given(userRepository).findByPhoneNumber(eq(newPhoneNumber)); //newPhoneNumber 전달 시, Optional.empty() 반환
    willReturn(Optional.of(existUser)).given(userRepository).findByPhoneNumber(eq(existPhoneNumber)); //existPhoneNumber 전달 시, 기존의 user 반환

    //WHEN
    UserInfoCheckResponse notExistResult = userService.checkPhoneNumberExist(newPhoneNumber);
    UserInfoCheckResponse existResult = userService.checkPhoneNumberExist(existPhoneNumber);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하지 않는 Phone Number 인 경우
        () -> assertFalse(notExistResult.isAlreadyExist()),
        //실패 케이스 - 1 - 존재하는 Phone Number 인 경우
        () -> assertTrue(existResult.isAlreadyExist())
    );
  }

}