/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : AuthController
 * author         : 우태균
 * date           : 2022/08/21
 * description    : 인증 API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.AuthApi;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.service.AuthService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairRequest;
import static com.heylocal.traveler.dto.AuthTokenDto.TokenPairResponse;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
  private final BindingErrorMessageProvider errorMessageProvider;
  private final AuthService authService;

  /**
   * 토큰 재발급 핸들러
   * @param request 기존의 토큰값들
   * @param bindingResult
   * @return
   * @throws BadRequestException Input 데이터 형식이 올바르지 않은 경우
   * @throws UnauthorizedException 재발급에 실패한 경우
   */
  @Override
  public TokenPairResponse reissueTokenPair(TokenPairRequest request, BindingResult bindingResult)
      throws BadRequestException, UnauthorizedException {
    TokenPairResponse response = null;

    if (bindingResult.hasFieldErrors()) {
      String errMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
    }

    response = authService.reissueTokenPair(request);

    return response;
  }
}
