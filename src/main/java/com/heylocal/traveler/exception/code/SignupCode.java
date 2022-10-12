/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : SigninCode
 * author         : 우태균
 * date           : 2022/08/25
 * description    : 회원가입 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 회원가입 관련 오류 코드
 * <pre/>
 */
public enum SignupCode implements ErrorCode {
  SHORT_OR_LONG_ACCOUNT_ID_LENGTH("계정 아이디는 5자 이상, 20자 이하이어야 합니다."),
  WRONG_ACCOUNT_ID_FORMAT("계정 아이디는 영문, 숫자 조합이어야 합니다."),
  WRONG_PHONE_NUMBER_FORMAT("휴대폰 번호 형식이 틀립니다. 하이픈 문자를 포함합니다."),
  WRONG_PASSWORD_FORMAT("비밀번호 형식이 틀립니다. 형식은 숫자 + 영어 + 특수문자 포함된 8자 이상입니다."),
  WRONG_NICKNAME_FORMAT("닉네임 형식이 틀립니다. 형식은 숫자 + 영어 조합, 2자 이상, 20자 이하입니다."),
  ALREADY_EXIST_USER_INFO("계정 ID가 이미 존재합니다.");

  private String description;

  SignupCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
