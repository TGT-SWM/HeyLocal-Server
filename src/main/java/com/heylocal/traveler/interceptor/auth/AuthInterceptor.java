package com.heylocal.traveler.interceptor.auth;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * 인가 관련 인터셉터
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
  private final JwtTokenParser jwtTokenParser;
  private final TravelerRepository travelerRepository;
  private final DispatcherServlet dispatcherServlet;

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
      responseError(response, AuthErrorStatus.EXPIRED_TOKEN);
    } catch (JwtException ex) {
      responseError(response, AuthErrorStatus.BAD_TOKEN);
    }

    /*
     * Request 객체의 attribute 에 LoginUser 담기.
     * 스프링 Argument Resolver 에서 사용될 수 있도록, HttpServletRequest 객체에 추가해야 한다.
     */
    addLoginUserToRequest(request, claims);

    return true;
  }

  /**
   * HttpServletRequest 객체에 로그인한 사용자(여행자) DTO 객체를 담는 메서드
   * @param request setAttribute() 로 담을 요청 객체
   * @param claims 토큰 파싱 결과 (로그인한 유저의 pk값이 들어있음)
   */
  private void addLoginUserToRequest(HttpServletRequest request, Claims claims) {
    Traveler traveler;
    Optional<Traveler> travelerOptional;
    LoginUser loginUser;

    /*
     * claim에 저장된 사용자 id(pk값)으로 DB에서 조회한다.
     * 사용자의 계정 id 나 닉네임이 변경될 수 있으므로, 매 요청마다 DB에서 조회해야 한다.
     */
    travelerOptional = travelerRepository.findById(claims.get("userPk", Long.class));
    if (travelerOptional.isEmpty()) {
      throw new IllegalArgumentException("존재하지 않는 사용자에 대한 pk값입니다.");
    }
    traveler = travelerOptional.get();

    //스프링 Argument Resolver에서 사용될 수 있도록, HttpServletRequest 객체에 추가한다.
    loginUser = LoginUser.builder()
        .id(traveler.getId())
        .accountId(traveler.getAccountId())
        .nickname(traveler.getNickname())
        .phoneNumber(traveler.getPhoneNumber())
        .userType(traveler.getUserType())
        .build();
    request.setAttribute("loginUser", loginUser);
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
      responseError(response, AuthErrorStatus.NO_HTTP_HEADER_VALUE);
      return null;
    }
    if (!authHeaderValue.startsWith("Bearer ")) { //Bearer 로 시작하지 않는 경우
      responseError(response, AuthErrorStatus.NOT_STARTS_WITH_BEARER);
      return null;
    }
    return authHeaderValue;
  }

  /**
   * <pre>
   * 인가 오류 응답 메시지를 만드는 메서드
   * </pre>
   * @param response 오류 응답 Response 객체
   * @param status 오류 종류
   * @throws IOException
   */
  private void responseError(HttpServletResponse response, AuthErrorStatus status) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    //JSON 형식으로 응답
    response.getWriter().write(
        "{" +
            "\"status\": \"" + status + "\",\n" +
            "\"msg\": \"" + status.getValue() + "\"" +
            "}"
    );
  }
}
