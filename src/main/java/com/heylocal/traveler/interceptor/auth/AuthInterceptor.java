package com.heylocal.traveler.interceptor.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.UnauthorizedCode;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 인가 관련 인터셉터
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
  private final ObjectMapper objectMapper;
  private final JwtTokenParser jwtTokenParser;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authHeaderValue;
    String accessTokenValue;
    Claims claims = null;

    //Authorization 헤더 확인
    authHeaderValue = getAuthHeaderValue(request, response);
    if (Objects.isNull(authHeaderValue)) return false;

    //Bearer 제거
    accessTokenValue = authHeaderValue.substring("Bearer ".length());

    //토큰 유효성 검증
    try {
      claims = jwtTokenParser.parseJwtToken(accessTokenValue).get();
    } catch (ExpiredJwtException ex) {
      responseError(response, UnauthorizedCode.EXPIRED_TOKEN);
      return false;
    } catch (JwtException ex) {
      responseError(response, UnauthorizedCode.BAD_TOKEN);
      return false;
    } catch (NoSuchElementException ex) {
      responseError(response, UnauthorizedCode.BAD_TOKEN);
      return false;
    }

    /*
     * Request 객체의 attribute 에 로그인한 사용자의 id값(pk) 담기.
     * 스프링 Argument Resolver 에서 사용될 수 있도록, HttpServletRequest 객체에 추가해야 한다.
     */
    request.setAttribute("userId", claims.get("userPk", Long.class));
    return true;
  }

  /**
   * Http 요청 메시지의 Authorization 헤더 값을 반환하는 메서드
   * @param request 요청 객체
   * @param response 응답 객체
   * @return 헤더 값, 유효하지 않은 값이라면 null 반환
   * @throws IOException
   */
  private String getAuthHeaderValue(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String authHeaderValue;

    authHeaderValue = request.getHeader("Authorization");
    if (Objects.isNull(authHeaderValue)) { //Authorization 헤더가 빈 경우
      responseError(response, UnauthorizedCode.NO_HTTP_HEADER_VALUE);
      return null;
    }
    if (!authHeaderValue.startsWith("Bearer ")) { //Bearer 로 시작하지 않는 경우
      responseError(response, UnauthorizedCode.NOT_STARTS_WITH_BEARER);
      return null;
    }
    return authHeaderValue;
  }

  /**
   * <pre>
   * 인가 오류 응답 메시지를 만드는 메서드
   * </pre>
   * @param response 오류 응답 Response 객체
   * @param code 오류 종류
   * @throws IOException
   */
  private void responseError(HttpServletResponse response, UnauthorizedCode code) throws IOException {
    ErrorMessageResponse errorResponse;
    String responseBody;

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    errorResponse = new ErrorMessageResponse(new UnauthorizedException(code));
    responseBody = objectMapper.writeValueAsString(errorResponse);


    //JSON 형식으로 응답
    response.getWriter().write(responseBody);
  }
}
