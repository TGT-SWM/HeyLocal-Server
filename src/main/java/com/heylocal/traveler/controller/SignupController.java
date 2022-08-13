package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SignupApi;
import com.heylocal.traveler.controller.exception.BadRequestException;
import com.heylocal.traveler.dto.Sample;
import com.heylocal.traveler.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;

@Slf4j
@Tag(name = "Signup")
@RestController
@RequiredArgsConstructor
public class SignupController implements SignupApi {
  private final UserService userService;

  @Override
  public UserInfoCheckResponse signupIdGet(String accountId) throws BadRequestException {
    UserInfoCheckResponse response;

    validateAccountIdFormat(accountId);
    response = userService.checkAccountIdExist(accountId);

    return response;
  }

  @Override
  public UserInfoCheckResponse signupPhoneNumGet(String phoneNumber) throws BadRequestException {
    UserInfoCheckResponse response;

    validatePhoneNumberFormat(phoneNumber);
    response = userService.checkPhoneNumberExist(phoneNumber);

    return response;
  }

  @Override
  public ResponseEntity<Void> signupPost(Sample body) {
    return null;
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
    String pattern = "^[a-zA-Z0-9]*$";

    if (accountId.length() < 5 || accountId.length() > 20) {
      throw new BadRequestException("계정 아이디는 5자 이상, 20자 이하이어야 합니다.");
    }
    if (!Pattern.matches(pattern, accountId)) {
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
    String pattern = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$";

    if (!Pattern.matches(pattern, phoneNumber)) {
      throw new BadRequestException("휴대폰 번호 형식이 틀립니다. 하이픈 문자를 포함합니다.");
    }

    return true;
  }
}
