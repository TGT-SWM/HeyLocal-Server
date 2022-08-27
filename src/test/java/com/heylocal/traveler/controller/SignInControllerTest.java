package com.heylocal.traveler.controller;

import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.exception.code.SigninCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.UnauthorizedException;
import com.heylocal.traveler.exception.service.SigninArgumentException;
import com.heylocal.traveler.service.SigninService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class SignInControllerTest {
  @Mock
  private SigninService signinService;
  @Mock
  private BindingResult bindingResult;
  private SigninController signinController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signinController = new SigninController(signinService);
  }

  @Test
  @DisplayName("로그인 컨트롤러")
  void signinPostTest() throws SigninArgumentException {
    //GIVEN
    String rightAccountId = "testAccountId";
    String rightRawPassword = "testPassword123!";
    long rightId = 3L;
    String rightNickname = "testNickname";
    String rightPhoneNumber = "010-1234-1234";
    UserRole rightUserRole = UserRole.TRAVELER;
    String rightAccessToken = "accessToken";
    String rightRefreshToken = "refreshToken";
    String emptyAccountId = "";
    String wrongRawPassword = "wrongTestPassword123!";
    SigninRequest rightRequest = SigninRequest.builder()
        .accountId(rightAccountId)
        .password(rightRawPassword)
        .build();
    SigninRequest wrongPwRequest = SigninRequest.builder()
        .accountId(rightAccountId)
        .password(wrongRawPassword)
        .build();
    SigninRequest emptyAccountIdRequest = SigninRequest.builder()
        .accountId(emptyAccountId)
        .password(rightRawPassword)
        .build();

    //Mock 행동 정의 - BindingResult
    willReturn(false).willReturn(true).willReturn(false).given(bindingResult).hasFieldErrors();

    //Mock 행동 정의 - SigninService
    SigninResponse rightResponse = SigninResponse.builder()
        .id(rightId)
        .accountId(rightAccountId)
        .nickname(rightNickname)
        .userRole(rightUserRole)
        .accessToken(rightAccessToken)
        .refreshToken(rightRefreshToken)
        .build();
    willReturn(rightResponse).given(signinService).signin(rightRequest);
    willThrow(new SigninArgumentException(SigninCode.WRONG_SIGNIN_PASSWORD)).given(signinService).signin(wrongPwRequest);

    //WHEN

    //THEN
    assertAll(
      //성공 케이스 - 1 - 정상 로그인
      () -> assertDoesNotThrow(() -> signinController.signinPost(rightRequest, bindingResult)),
      //실패 케이스 - 1 - 빈 계정 id일 때
      () -> assertThrows(BadRequestException.class, () -> signinController.signinPost(emptyAccountIdRequest, bindingResult)),
      //실패 케이스 - 2 - 잘못된 pw일 때
      () -> assertThrows(UnauthorizedException.class, () -> signinController.signinPost(wrongPwRequest, bindingResult))
    );
  }
}