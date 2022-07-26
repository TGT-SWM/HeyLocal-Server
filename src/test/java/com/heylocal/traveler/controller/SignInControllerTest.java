package com.heylocal.traveler.controller;

import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.AuthTokenDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.SigninCode;
import com.heylocal.traveler.service.SigninService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class SignInControllerTest {
  @Mock
  private BindingErrorMessageProvider messageProvider;
  @Mock
  private SigninService signinService;
  @Mock
  private BindingResult bindingResult;
  private SigninController signinController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.signinController = new SigninController(messageProvider, signinService);
  }

  @Test
  @DisplayName("로그인 컨트롤러")
  void signinPostTest() throws UnauthorizedException {
    //GIVEN
    String rightAccountId = "testAccountId";
    String rightRawPassword = "testPassword123!";
    long rightId = 3L;
    String rightNickname = "testNickname";
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
    FieldError fieldError = new FieldError(AuthTokenDto.TokenPairRequest.class.getName(), "refreshToken", "빈값입니다.");
    willReturn(fieldError).given(bindingResult).getFieldError();

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
    willThrow(new UnauthorizedException(SigninCode.WRONG_SIGNIN_PASSWORD)).given(signinService).signin(wrongPwRequest);

    //WHEN

    //THEN
    assertAll(
      //성공 케이스 - 1 - 정상 로그인
      () -> assertDoesNotThrow(() -> signinController.signin(rightRequest, bindingResult)),
      //실패 케이스 - 1 - 빈 계정 id일 때
      () -> assertThrows(BadRequestException.class, () -> signinController.signin(emptyAccountIdRequest, bindingResult)),
      //실패 케이스 - 2 - 잘못된 pw일 때
      () -> assertThrows(UnauthorizedException.class, () -> signinController.signin(wrongPwRequest, bindingResult))
    );
  }
}