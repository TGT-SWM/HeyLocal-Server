package com.heylocal.traveler.controller.resolver;

import com.heylocal.traveler.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName    : com.heylocal.traveler.controller.resolver
 * fileName       : LoginUserResolver
 * author         : 우태균
 * date           : 2022/08/18
 * description    : 로그인 정보 처리를 위한 Argument Resolver
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(LoginUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    long userId;
    HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

    userId = (long) httpServletRequest.getAttribute("userId");
    return LoginUser.builder()
        .id(userId)
        .build();
  }
}
