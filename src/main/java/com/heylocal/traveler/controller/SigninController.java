package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SigninApi;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.UnauthorizedException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.service.SigninService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : SigninController
 * author         : 우태균
 * date           : 2022/08/15
 * description    : 로그인 API 컨트롤러
 */

@Slf4j
@Tag(name = "Signin")
@RestController
@RequiredArgsConstructor
public class SigninController implements SigninApi {
  private final BindingErrorMessageProvider errorMessageProvider;
  private final SigninService signinService;

  /**
   * 로그인 핸들러
   * @param request 로그인 정보
   * @param bindingResult
   * @return
   * @throws BadRequestException Input 데이터 형식이 올바르지 않은 경우
   * @throws UnauthorizedException 로그인 정보가 틀린 경우
   */
  @Override
  public SigninResponse signin(SigninRequest request, BindingResult bindingResult) throws BadRequestException, UnauthorizedException {
    SigninResponse response = null;

    if (bindingResult.hasFieldErrors()) {
      String errMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
    }

    response = signinService.signin(request);

    return response;
  }

}
