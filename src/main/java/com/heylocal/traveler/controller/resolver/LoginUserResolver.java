package com.heylocal.traveler.controller.resolver;

import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {
  private final AuthService authService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    log.info("{}", parameter.getParameterType().equals(LoginUser.class));
    return parameter.getParameterType().equals(LoginUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    long userId;
    LoginUser loginUser;
    HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

    userId = (long) httpServletRequest.getAttribute("userId");
    loginUser = authService.findLoginUser(userId);

    return loginUser;
  }
}
