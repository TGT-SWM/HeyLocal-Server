package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.AuthApi;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.UnauthorizedException;
import com.heylocal.traveler.exception.service.AuthException;
import com.heylocal.traveler.service.AuthService;
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
  private final AuthService authService;

  @Override
  public TokenPairResponse reissueTokenPair(TokenPairRequest request, BindingResult bindingResult)
      throws BadRequestException, UnauthorizedException {
    TokenPairResponse response = null;

    if (bindingResult.hasFieldErrors()) {
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM);
    }
    try {
      response = authService.reissueTokenPair(request);
    } catch (AuthException e) {
      throw new UnauthorizedException(e.getCode());
    }

    return response;
  }
}
