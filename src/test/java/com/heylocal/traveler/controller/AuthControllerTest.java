package com.heylocal.traveler.controller;

import com.heylocal.traveler.exception.code.AuthCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.UnauthorizedException;
import com.heylocal.traveler.exception.service.AuthException;
import com.heylocal.traveler.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairRequest;
import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
  @Mock
  private AuthService authService;
  private AuthController authController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    authController = new AuthController(authService);
  }

  @Test
  @DisplayName("토큰 재발급 컨트롤러")
  void tokenPutTest(@Mock BindingResult bindingResult) throws AuthException, UnauthorizedException, BadRequestException {
    //GIVEN
    String expiredAccessTokenValue = "expiredAccessTokenValue"; //만료된 Access Token
    String notExpiredAccessTokenValue = "notExpiredAccessTokenValue"; //만료되지 않은 Access Token
    String notMatchedAccessTokenValue = "notMatchedAccessTokenValue"; //notExpiredRefreshTokenValue 와 매치되지 않는 Access Token
    String notExpiredRefreshTokenValue = "notExpiredRefreshTokenValue"; //만료되지 않은 Refresh Token
    String expiredRefreshTokenValue = "expiredRefreshTokenValue"; //만료된 Refresh Token
    String notExistRefreshTokenValue = "notExistRefreshTokenValue"; //존재하지 않는 Refresh Token
    String emptyRefreshTokenValue = ""; //빈 Refresh Token

    TokenPairRequest succeedReissueRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();
    TokenPairRequest notExpiredAccessTokenRequest = TokenPairRequest.builder()
        .accessToken(notExpiredAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();
    TokenPairRequest expiredRefreshTokenRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(expiredRefreshTokenValue)
        .build();
    TokenPairRequest notMatchedTokenRequest = TokenPairRequest.builder()
        .accessToken(notMatchedAccessTokenValue)
        .refreshToken(notExpiredRefreshTokenValue)
        .build();
    TokenPairRequest notExistRefreshTokenRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(notExistRefreshTokenValue)
        .build();
    TokenPairRequest emptyRefreshTokenRequest = TokenPairRequest.builder()
        .accessToken(expiredAccessTokenValue)
        .refreshToken(emptyRefreshTokenValue)
        .build();

    String reissuedAccessToken = "reissuedAccessToken";
    String reissuedRefreshToken = "reissuedRefreshToken";
    TokenPairResponse succeedResponse = TokenPairResponse.builder()
        .accessToken(reissuedAccessToken)
        .refreshToken(reissuedRefreshToken)
        .build();

    //Mock 행동 정의 - AuthService
    willReturn(succeedResponse)
        .given(authService).reissueTokenPair(succeedReissueRequest);
    willThrow(new AuthException(AuthCode.NOT_EXPIRED_ACCESS_TOKEN))
        .given(authService).reissueTokenPair(notExpiredAccessTokenRequest);
    willThrow(new AuthException(AuthCode.EXPIRED_REFRESH_TOKEN))
        .given(authService).reissueTokenPair(expiredRefreshTokenRequest);
    willThrow(new AuthException(AuthCode.NOT_MATCH_PAIR))
        .given(authService).reissueTokenPair(notMatchedTokenRequest);
    willThrow(new AuthException(AuthCode.NOT_EXIST_REFRESH_TOKEN))
        .given(authService).reissueTokenPair(notExistRefreshTokenRequest);

    //Mock 행동 정의 - BindingResult
    willReturn(false).willReturn(true).willReturn(false)
        .given(bindingResult).hasFieldErrors();

    //WHEN
    TokenPairResponse succeedResult = authController.tokenPut(succeedReissueRequest, bindingResult);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 만료된 Access Token, 만료안된 Refresh Token
        () -> assertEquals(succeedResponse, succeedResult),
        //실패 케이스 - 1 - Request 로 빈 값이 전달되었을 때
        () -> assertThrows(BadRequestException.class, () -> authController.tokenPut(emptyRefreshTokenRequest, bindingResult)),
        //실패 케이스 - 2 - 아직 만료안된 Access Token, 만료안된 Refresh Token
        () -> assertThrows(UnauthorizedException.class,
            () -> authController.tokenPut(notExpiredAccessTokenRequest, bindingResult),
            AuthCode.NOT_EXPIRED_ACCESS_TOKEN.getDescription()
        ),
        //실패 케이스 - 3 - 만료된 Access Token, 만료된 Refresh Token
        () -> assertThrows(UnauthorizedException.class,
            () -> authController.tokenPut(expiredRefreshTokenRequest, bindingResult),
            AuthCode.EXPIRED_REFRESH_TOKEN.getDescription()
        ),
        //실패 케이스 - 4 - Refresh Token 과 매치되지 않는 Access Token, 만료안된 Refresh Token
        () -> assertThrows(UnauthorizedException.class,
            () -> authController.tokenPut(notMatchedTokenRequest, bindingResult),
            AuthCode.NOT_MATCH_PAIR.getDescription()
        ),
        //실패 케이스 - 5 - 만료된 Access Token, 존재하지 않는 Refresh Token
        () -> assertThrows(UnauthorizedException.class,
            () -> authController.tokenPut(notExistRefreshTokenRequest, bindingResult),
            AuthCode.NOT_EXIST_REFRESH_TOKEN.getDescription()
        )
    );
  }
}