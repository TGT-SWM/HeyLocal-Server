package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SignupApi;
import com.heylocal.traveler.controller.exception.BadRequestException;
import com.heylocal.traveler.service.SignupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

import static com.heylocal.traveler.dto.SignupDto.*;
import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;

@Slf4j
@Tag(name = "Signup")
@RestController
@RequiredArgsConstructor
public class SignupController implements SignupApi {

  private final SignupService signupService;

  @Value("${heylocal.signup.pattern.account-id}")
  private String accountIdPattern;
  @Value("${heylocal.signup.pattern.phone-number}")
  private String phoneNumberPattern;
  @Value("${heylocal.signup.pattern.password}")
  private String passwordPattern;
  @Value("${heylocal.signup.pattern.nickname}")
  private String nicknamePattern;

  @Override
  public UserInfoCheckResponse signupIdGet(String accountId) throws BadRequestException {
    UserInfoCheckResponse response;

    validateAccountIdFormat(accountId);
    response = signupService.checkAccountIdExist(accountId);

    return response;
  }

  @Override
  public UserInfoCheckResponse signupPhoneNumGet(String phoneNumber) throws BadRequestException {
    UserInfoCheckResponse response;

    validatePhoneNumberFormat(phoneNumber);
    response = signupService.checkPhoneNumberExist(phoneNumber);

    return response;
  }

  @Override
    public void signupPost(SignupRequest request) throws BadRequestException {
    validateAccountIdFormat(request.getAccountId()); //계정 포맷 검증
    validatePasswordFormat(request.getPassword()); //비밀번호 포맷 검증
    validateNicknameFormat(request.getNickname()); //닉네임 포맷 검증
    validatePhoneNumberFormat(request.getPhoneNumber()); //전화번호 포맷 검증

    signupService.signupTraveler(request);
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
      throw new BadRequestException("계정 아이디는 5자 이상, 20자 이하이어야 합니다.");
    }
    if (!Pattern.matches(accountIdPattern, accountId)) {
      throw new BadRequestException("계정 아이디는 영문, 숫자 조합이어야 합니다.");
    }

    return true;
  }

  /**
   * <pre>
   * 휴대폰 번호 형식 검증
   * 13자리, 하이픈 필수
   * </pre>
   * @param phoneNumber 검증할 휴대폰 번호
   * @return 조건에 부합하면 true 반환
   * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
   */
  private boolean validatePhoneNumberFormat(String phoneNumber) throws BadRequestException {
    if (!Pattern.matches(phoneNumberPattern, phoneNumber)) {
      throw new BadRequestException("휴대폰 번호 형식이 틀립니다. 하이픈 문자를 포함합니다.");
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
      throw new BadRequestException("비밀번호 형식이 틀립니다.");
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
      throw new BadRequestException("비밀번호 형식이 틀립니다.");
    }

    return true;
  }
}
