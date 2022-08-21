package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 토큰 자체 관련 오류 코드
 * <pre/>
 */
public enum TokenCode implements ErrorCode {
  NOT_EXIST_TOKEN_USER_ID("Token 클레임의 사용자 pk 값이 존재하지 않는 값입니다.");

  private String description;

  TokenCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
