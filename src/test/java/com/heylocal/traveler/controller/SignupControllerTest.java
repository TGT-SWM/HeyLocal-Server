package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.exception.BadRequestException;
import com.heylocal.traveler.dto.SignupDto;
import com.heylocal.traveler.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

@Slf4j
class SignupControllerTest {
  //계정 ID 검증 - 영문, 숫자 조합인지 (숫자 없어도 됨)
  private String accountIdPattern = "^[a-zA-Z0-9]*$";
  //휴대폰 번호 검증 - 13자리, 하이픈 필수
  private String phoneNumberPattern = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$";
  //비밀번호 검증 - 숫자 + 영어 + 특수문자 포함된 8자 이상
  private String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
  //닉네임 검증 - 2자 이상, 20자 이하
  private String nicknamePattern = "^[a-zA-Z0-9]{2,20}$";

  @Mock
  private SignupService signupService;
  private SignupController signupController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signupController = new SignupController(signupService);

    //단위테스트인 경우, @Value 를 통해 값을 주입하지 못하므로 Reflection 을 통해 필드 값을 설정한다.
    ReflectionTestUtils.setField(
        signupController,
        "accountIdPattern",
        accountIdPattern
    );
    ReflectionTestUtils.setField(
        signupController,
        "phoneNumberPattern",
        phoneNumberPattern
    );
    ReflectionTestUtils.setField(
        signupController,
        "passwordPattern",
        passwordPattern
    );
    ReflectionTestUtils.setField(
        signupController,
        "nicknamePattern",
        nicknamePattern
    );
  }

  @Test
  @DisplayName("아이디 중복 확인 컨트롤러")
  void signupIdGet() {
    //GIVEN
    String notExist_RightAccountId = "abcd123";
    String notExist_Less5AccountId = "abc";
    String notExist_Less5WithNumberAccountId = "ab2";
    String notExist_Over20AccountId = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String notExist_Over20WithNumberAccountId = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1234";
    String notExist_WithSpecialSymbolAccountId = "abcd123$!@";
    String exist_RightAccountId = "qwerty123";
    UserInfoCheckResponse existResponse = new UserInfoCheckResponse(true);
    UserInfoCheckResponse notExistResponse = new UserInfoCheckResponse(false);

    //Mock 행동 정의
    willReturn(notExistResponse).given(signupService).checkAccountIdExist(not(eq(exist_RightAccountId))); //exist_RightAccountId 가 아닌 모든 경우, notExistResponse 반환
    willReturn(existResponse).given(signupService).checkAccountIdExist(eq(exist_RightAccountId)); //exist_RightAccountId 인 경우, existResponse 반환

    //WHEN-THEN
    assertAll(
        //성공 케이스 - 1 - 포맷이 맞고, 존재하지 않는 계정 ID인 경우
        () -> assertDoesNotThrow(() -> {
          UserInfoCheckResponse response = signupController.signupIdGet(notExist_RightAccountId);
          assertFalse(response.isAlreadyExist());
        }),
        //성공 케이스 - 2 - 포맷이 맞고, 존재하는 계정 ID인 경우
        () -> assertDoesNotThrow(() -> {
          UserInfoCheckResponse response = signupController.signupIdGet(exist_RightAccountId);
          assertTrue(response.isAlreadyExist());
        }),
        //실패 케이스 - 1 - 존재하지 않고, 5자 미만인 계정 ID인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupIdGet(notExist_Less5AccountId)),
        //실패 케이스 - 2 - 존재하지 않고, 5자 미만이고 영어숫자 조합인 계정 ID인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupIdGet(notExist_Less5WithNumberAccountId)),
        //실패 케이스 - 3 - 존재하지 않고, 20자 초과인 계정 ID인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupIdGet(notExist_Over20AccountId)),
        //실패 케이스 - 4 - 존재하지 않고, 20자 초과이고 영어숫자 조합인 계정 ID인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupIdGet(notExist_Over20WithNumberAccountId)),
        //실패 케이스 - 5 - 존재하지 않고, 특수기호가 들어간 계정 ID인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupIdGet(notExist_WithSpecialSymbolAccountId))
    );

  }

  @Test
  @DisplayName("휴대폰 번호 중복 확인 컨트롤러")
  void signupPhoneNumGetTest() {
    //GIVEN
    String notExist_RightPhoneNumber = "010-1234-1234";
    String notExist_NoHyphenPhoneNumber = "01012341234";
    String notExist_LongPhoneNumber = "010-123412-341234";
    String notExist_LongNoHyphenPhoneNumber = "010123412341234";
    String notExist_ShortPhoneNumber = "010-12-12";
    String notExist_ShortNoHyphenPhoneNumber = "0101212";
    String exist_RightPhoneNumber = "010-1111-1111";
    UserInfoCheckResponse notExistResponse = new UserInfoCheckResponse(false);
    UserInfoCheckResponse existResponse = new UserInfoCheckResponse(true);

    //Mock 행동 정의
    willReturn(notExistResponse).given(signupService).checkPhoneNumberExist(not(eq(exist_RightPhoneNumber))); //exist_RightPhoneNumber 가 아닌 모든 경우에 notExistResponse 반환
    willReturn(existResponse).given(signupService).checkPhoneNumberExist(eq(exist_RightPhoneNumber)); //exist_RightPhoneNumber 인 경우에 existResponse 반환

    //WHEN-THEN
    assertAll(
        //성공 케이스 - 1 - 포맷이 맞고, 존재하지 않는 번호인 경우
        () -> assertDoesNotThrow(() -> {
          UserInfoCheckResponse response = signupController.signupPhoneNumGet(notExist_RightPhoneNumber);
          assertFalse(response.isAlreadyExist());
        }),
        //성공 케이스 - 2 - 포맷이 맞고, 존재하는 번호인 경우
        () -> assertDoesNotThrow(() -> {
          UserInfoCheckResponse response = signupController.signupPhoneNumGet(exist_RightPhoneNumber);
          assertTrue(response.isAlreadyExist());
        }),
        //실패 케이스 - 1 - 존재하지 않고, 하이픈이 없는 번호인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPhoneNumGet(notExist_NoHyphenPhoneNumber)),
        //실패 케이스 - 2 - 존재하지 않고, 긴 번호인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPhoneNumGet(notExist_LongPhoneNumber)),
        //실패 케이스 - 3 - 존재하지 않고, 길고 하이픈이 없는 번호인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPhoneNumGet(notExist_LongNoHyphenPhoneNumber)),
        //실패 케이스 - 4 - 존재하지 않고, 짧은 번호인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPhoneNumGet(notExist_ShortPhoneNumber)),
        //실패 케이스 - 5 - 존재하지 않고, 짧고 하이픈이 없는 번호인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPhoneNumGet(notExist_ShortNoHyphenPhoneNumber))
    );
  }

  @Test
  @DisplayName("회원가입 컨트롤러")
  void signupPostTest() {
    //GIVEN
    String rightAccountId = "testAccountId";
    String wrongAccountId = "testAccountId!@#";
    String rightRawPassword = "testPassword123!@#";
    String wrongRawPassword = "testPassword";
    String rightNickname = "testNickname";
    String wrongNickname = "testNickname#@!";
    String rightPhoneNumber = "010-1111-1111";
    String wrongPhoneNumber = "01011111111";
    SignupDto.SignupRequest rightRequest = SignupDto.SignupRequest.builder()
        .accountId(rightAccountId)
        .password(rightRawPassword)
        .nickname(rightNickname)
        .phoneNumber(rightPhoneNumber)
        .build();
    SignupDto.SignupRequest wrongAccountIdRequest = SignupDto.SignupRequest.builder()
        .accountId(wrongAccountId)
        .password(rightRawPassword)
        .nickname(rightNickname)
        .phoneNumber(rightPhoneNumber)
        .build();
    SignupDto.SignupRequest wrongPasswordRequest = SignupDto.SignupRequest.builder()
        .accountId(rightAccountId)
        .password(wrongRawPassword)
        .nickname(rightNickname)
        .phoneNumber(rightPhoneNumber)
        .build();
    SignupDto.SignupRequest wrongNicknameRequest = SignupDto.SignupRequest.builder()
        .accountId(rightAccountId)
        .password(rightRawPassword)
        .nickname(wrongNickname)
        .phoneNumber(rightPhoneNumber)
        .build();
    SignupDto.SignupRequest wrongPhoneNumberRequest = SignupDto.SignupRequest.builder()
        .accountId(rightAccountId)
        .password(rightRawPassword)
        .nickname(rightNickname)
        .phoneNumber(wrongPhoneNumber)
        .build();

    //WHEN


    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> signupController.signupPost(rightRequest)),
        //실패 케이스 - 1 - 잘못된 포맷의 계정 ID 인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPost(wrongAccountIdRequest)),
        //실패 케이스 - 2 - 잘못된 포맷의 비밀번호 인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPost(wrongPasswordRequest)),
        //실패 케이스 - 3 - 잘못된 포맷의 닉네임 인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPost(wrongNicknameRequest)),
        //실패 케이스 - 4 - 잘못된 포맷의 휴대폰 번호 인 경우
        () -> assertThrows(BadRequestException.class, () -> signupController.signupPost(wrongPhoneNumberRequest))
    );
  }
}