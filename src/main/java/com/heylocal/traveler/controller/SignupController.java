package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SignupApi;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.SignupCode;
import com.heylocal.traveler.service.SignupService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

import static com.heylocal.traveler.dto.SignupDto.SignupRequest;
import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;

/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : SignupController
 * author         : 신우진
 * date           : 2022/08/13
 * description    : 회원가입 API 컨트롤러
 */

@Slf4j
@Tag(name = "Signup")
@RestController
@RequiredArgsConstructor
public class SignupController implements SignupApi {
  private final BindingErrorMessageProvider errorMessageProvider;

  private final SignupService signupService;

  @Value("${heylocal.signup.pattern.account-id}")
  private String accountIdPattern;
  @Value("${heylocal.signup.pattern.password}")
  private String passwordPattern;
  @Value("${heylocal.signup.pattern.nickname}")
  private String nicknamePattern;

  /**
   * 계정 아이디 중복 확인 핸들러
   * @param accountId 확인할 아이디 값
   * @return
   * @throws BadRequestException 아이디 형식이 잘못된 경우
   */
  @Override
  public UserInfoCheckResponse checkSignupId(String accountId) throws BadRequestException {
    UserInfoCheckResponse response;

    validateAccountIdFormat(accountId);
    response = signupService.checkAccountIdExist(accountId);

    return response;
  }

  /**
   * 회원가입 핸들러
   * @param request 회원가입 정보
   * @param bindingResult
   * @throws BadRequestException Input 데이터 형식이 올바르지 않은 경우
   */
  @Override
  public void signup(SignupRequest request, BindingResult bindingResult) throws BadRequestException {
    if (bindingResult.hasFieldErrors()) {
      String errMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
    }
    validateAccountIdFormat(request.getAccountId()); //계정 포맷 검증
    validatePasswordFormat(request.getPassword()); //비밀번호 포맷 검증
    validateNicknameFormat(request.getNickname()); //닉네임 포맷 검증

    signupService.signupUser(request);

  }

  /**
   * <pre>
   * 계정 ID 형식 검증
   * 5자 이상, 20자 이하 인지
   * 영문, 숫자 조합인지 (숫자 없어도 됨)
   * @param accountId 검증할 계정 ID
   * @return 조건에 부합하면 true 반환
   * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
   * </pre>
   */
  private boolean validateAccountIdFormat(String accountId) throws BadRequestException {
    if (accountId.length() < 5 || accountId.length() > 20) {
      throw new BadRequestException(SignupCode.SHORT_OR_LONG_ACCOUNT_ID_LENGTH);
    }
    if (!Pattern.matches(accountIdPattern, accountId)) {
      throw new BadRequestException(SignupCode.WRONG_ACCOUNT_ID_FORMAT);
    }

    return true;
  }

  /**
   * <pre>
   * 비밀번호 형식 검증
   * 숫자 + 영어 + 특수문자 포함된 8자 이상
   * </pre>
   * @param rawPassword 검증할 비밀번호
   * @return 조건에 부합하면 true 반환
   * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
   */
  private boolean validatePasswordFormat(String rawPassword) throws BadRequestException {
    if (!Pattern.matches(passwordPattern, rawPassword)) {
      throw new BadRequestException(SignupCode.WRONG_PASSWORD_FORMAT);
    }

    return true;
  }

  /**
   * <pre>
   * 닉네임 형식 검증
   * 숫자 + 영어 조합, 2자 이상, 20자 이하
   * </pre>
   * @param nickname 검증할 닉네임
   * @return 조건에 부합하면 true 반환
   * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
   */
  private boolean validateNicknameFormat(String nickname) throws BadRequestException {
    if (!Pattern.matches(nicknamePattern, nickname)) {
      throw new BadRequestException(SignupCode.WRONG_NICKNAME_FORMAT);
    }

    return true;
  }
}
