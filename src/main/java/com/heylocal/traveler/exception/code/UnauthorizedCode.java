package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 인가 관련 오류 코드
 * <pre/>
 */
public enum UnauthorizedCode implements ErrorCode {
  NO_HTTP_HEADER_VALUE("Authorization HTTP 헤더에 값이 없습니다."),
  NOT_STARTS_WITH_BEARER("Authorization HTTP 헤더의 값은 Bearer 로 시작해야 합니다."),
  EXPIRED_TOKEN("토큰 유효기간이 만료되었습니다."),
  BAD_TOKEN("토큰 값이 잘못되었습니다.");

  private String description;

  UnauthorizedCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
