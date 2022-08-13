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

import static com.heylocal.traveler.dto.SignupDto.IdCheckResponse;

@Slf4j
@Tag(name = "Signup")
@RestController
@RequiredArgsConstructor
public class SignupController implements SignupApi {
  private final UserService userService;

  @Override
  public IdCheckResponse signupIdGet(String accountId) throws BadRequestException {
    validateAccountId(accountId);
    IdCheckResponse response = userService.checkAccountIdExist(accountId);
    return response;
  }

  @Override
  public ResponseEntity<Void> signupPhoneNumGet(String phoneNumber) {
    return null;
  }

  @Override
  public ResponseEntity<Void> signupPost(Sample body) {
    return null;
  }

  /**
   * <pre>
   * 계정 ID 검증
   * 5자 이상, 20자 이하 인지
   * 영문, 숫자 조합인지 (숫자 없어도 됨)
   * @param accountId 검증할 계정 ID
   * @return 조건에 부합하면 true 반환
   * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
   * </pre>
   */
  private boolean validateAccountId(String accountId) throws BadRequestException {
    String pattern = "^[a-zA-Z0-9]*$";
    if (accountId.length() < 5 || accountId.length() > 20) {
      throw new BadRequestException("계정 아이디는 5자 이상, 20자 이하이어야 합니다.");
    }
    if (!Pattern.matches(pattern, accountId)) {
      throw new BadRequestException("계정 아이디는 영문, 숫자 조합이어야 합니다.");
    }

    return true;
  }
}
