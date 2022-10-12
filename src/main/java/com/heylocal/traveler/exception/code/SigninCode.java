/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : SigninCode
 * author         : 우태균
 * date           : 2022/08/25
 * description    : 로그인 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

public enum SigninCode implements ErrorCode {
  NOT_EXIST_SIGNIN_ACCOUNT_ID("계정 ID가 존재하지 않습니다."),
  WRONG_SIGNIN_PASSWORD("로그인 비밀번호가 틀립니다.");

  private String description;

  SigninCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
