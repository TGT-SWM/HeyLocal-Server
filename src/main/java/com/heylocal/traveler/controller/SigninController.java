package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SigninApi;
import com.heylocal.traveler.controller.exception.BadRequestException;
import com.heylocal.traveler.controller.exception.UnauthorizedException;
import com.heylocal.traveler.service.SigninService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

@Slf4j
@Tag(name = "Signin")
@RestController
@RequiredArgsConstructor
public class SigninController implements SigninApi {

  private final SigninService signinService;

  @Override
  public SigninResponse signinPost(SigninRequest request, BindingResult bindingResult) throws BadRequestException, UnauthorizedException {
    if (bindingResult.hasFieldErrors()) {
      throw new BadRequestException("아이디나 비밀번호가 비어있을 수 없습니다.");
    }

    SigninResponse response = signinService.signin(request);

    return response;
  }

}
