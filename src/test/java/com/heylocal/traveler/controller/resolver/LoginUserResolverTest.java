package com.heylocal.traveler.controller.resolver;

import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class LoginUserResolverTest {
  @Mock
  private AuthService authService;
  @Mock
  private MethodParameter parameter;
  @Mock
  private ModelAndViewContainer mavContainer;
  @Mock
  private WebDataBinderFactory binderFactory;
  @Mock
  private NativeWebRequest webRequest;
  private LoginUserResolver loginUserResolver;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); //여러 test code 실행 시, mock 객체의 정의된 행동이 꼬일 수 있으므로 초기화한다.
    this.loginUserResolver = new LoginUserResolver(authService);
  }

  @Test
  @DisplayName("파라미터 지원 여부 확인")
  void supportsParameterTest() throws NoSuchMethodException {
    //GIVEN
    Method testMethod = LoginUserResolverTest.class.getDeclaredMethod("testMethod", LoginUser.class, String.class);
    MethodParameter methodParameterLoginUser = new MethodParameter(testMethod, 0);
    MethodParameter methodParameterString = new MethodParameter(testMethod, 1);

    //WHEN
    boolean rightResult = loginUserResolver.supportsParameter(methodParameterLoginUser);
    boolean wrongResult = loginUserResolver.supportsParameter(methodParameterString);

    //THEN
    assertAll(
        () -> assertTrue(rightResult),
        () -> assertFalse(wrongResult)
    );
  }

  @Test
  @DisplayName("ArgumentResolver")
  void resolveArgumentTest() throws Exception {
    //GIVEN
    String phoneNumber = "010-1234-1234";
    String nickname = "testNickname";
    String accountId = "testAccountId";
    long userId = 3l;
    LoginUser loginUser = LoginUser.builder()
        .userType(UserType.TRAVELER)
        .phoneNumber(phoneNumber)
        .nickname(nickname)
        .accountId(accountId)
        .id(userId)
        .build();
    HttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("userId", userId);

    //Mock 행동 정의 - NativeWebRequest
    willReturn(request).given(webRequest).getNativeRequest();
    //Mock 행동 정의 - AuthServer
    willReturn(loginUser).given(authService).findLoginTraveler(userId);

    //WHEN
    LoginUser result = (LoginUser) loginUserResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> loginUserResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory)),
        //성공 케이스 - 2
        () -> assertEquals(loginUser, result)
    );
  }

  private void testMethod(LoginUser myParam1, String myParam2) {

  }
}