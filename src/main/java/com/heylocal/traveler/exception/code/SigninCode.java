package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 로그인 관련 오류 코드
 * <pre/>
 */
public enum SigninCode implements ErrorCode {
  NOT_EXIST_SIGNIN_ACCOUNT_ID("존재하지 않는 계정 ID로 로그인할 수 없습니다."),
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
