package com.heylocal.traveler.interceptor.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.repository.redis.AccessTokenRedisRepository;
import com.heylocal.traveler.util.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {
  @Mock
  private AccessTokenRedisRepository accessTokenRedisRepository;
  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private JwtTokenParser jwtTokenParser;
  private AuthInterceptor authInterceptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.authInterceptor = new AuthInterceptor(accessTokenRedisRepository, objectMapper, jwtTokenParser);
  }

  @Test
  @DisplayName("유효한 토큰이 전달된 경우")
  void preHandleValidTokenTest(@Mock HttpServletRequest request) throws Exception {
    //GIVEN
    String validTokenValue = "validValue";
    String validAuthHeaderValue = "Bearer " + validTokenValue;
    long validUserId = 3L;
    HttpServletResponse response = new MockHttpServletResponse();
    Claims claims = new DefaultClaims();

    claims.setExpiration(new Date(new Date().getTime() + 1000000));
    claims.put("userPk", validUserId);

    //Mock 행동 정의 - JwtTokenParser
    willReturn(Optional.of(claims)).given(jwtTokenParser).parseJwtToken(eq(validTokenValue));

    //Mock 행동 정의 - HttpServletRequest
    willReturn(validAuthHeaderValue).given(request).getHeader(eq("Authorization"));

    //Mock 행동 정의 - accessTokenRedisRepository
    willReturn(Optional.of(new AccessToken())).given(accessTokenRedisRepository).findByUserId(validUserId);

    //WHEN
    boolean result = authInterceptor.preHandle(request, response, null);

    //THEN
    assertTrue(result);
  }

  @Test
  @DisplayName("Bearer 가 없을 때")
  void preHandleNoBearerTest(@Mock HttpServletRequest request) throws Exception {
    //GIVEN
    String validTokenValue = "validValue";
    String invalidAuthHeaderValue = validTokenValue;
    HttpServletResponse response = new MockHttpServletResponse();

    //Mock 행동 정의 - HttpServletRequest
    willReturn(invalidAuthHeaderValue).given(request).getHeader(eq("Authorization"));

    //Mock 행동 정의 - ObjectMapper
    String errorBody = "errorBody";
    willReturn(errorBody).given(objectMapper).writeValueAsString(any());

    //WHEN
    boolean result = authInterceptor.preHandle(request, response, null);

    //THEN
    assertAll(
        () -> assertFalse(result),
        () -> assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus())
    );
  }

  @Test
  @DisplayName("Authorization 헤더에 Bearer는 있고, 토큰 값이 없을 때")
  void preHandleNoTokenValueTest(@Mock HttpServletRequest request) throws Exception {
    //GIVEN
    String emptyTokenValue = "";
    String invalidAuthHeaderValue = "Bearer " + emptyTokenValue;
    HttpServletResponse response = new MockHttpServletResponse();

    //Mock 행동 정의 - HttpServletRequest
    willReturn(invalidAuthHeaderValue).given(request).getHeader(eq("Authorization"));

    //Mock 행동 정의 - ObjectMapper
    String errorBody = "errorBody";
    willReturn(errorBody).given(objectMapper).writeValueAsString(any());

    //WHEN
    boolean result = authInterceptor.preHandle(request, response, null);

    //THEN
    assertAll(
        () -> assertFalse(result),
        () -> assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus())
    );
  }

  @Test
  @DisplayName("만료된 토큰이 전달된 경우")
  void preHandleExpiredTokenTest(@Mock HttpServletRequest request) throws Exception {
    //GIVEN
    String expiredTokenValue = "validValue";
    String invalidAuthHeaderValue = "Bearer " + expiredTokenValue;
    long validUserId = 3L;
    HttpServletResponse response = new MockHttpServletResponse();
    Claims claims = new DefaultClaims();

    claims.setExpiration(new Date(new Date().getTime() - 1000000));
    claims.put("userPk", validUserId);

    //Mock 행동 정의 - JwtTokenParser
    willThrow(ExpiredJwtException.class).given(jwtTokenParser).parseJwtToken(eq(expiredTokenValue));

    //Mock 행동 정의 - HttpServletRequest
    willReturn(invalidAuthHeaderValue).given(request).getHeader(eq("Authorization"));

    //Mock 행동 정의 - ObjectMapper
    String errorBody = "errorBody";
    willReturn(errorBody).given(objectMapper).writeValueAsString(any());

    //WHEN
    boolean result = authInterceptor.preHandle(request, response, null);

    //THEN
    assertAll(
        () -> assertFalse(result),
        () -> assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus())
    );
  }
}