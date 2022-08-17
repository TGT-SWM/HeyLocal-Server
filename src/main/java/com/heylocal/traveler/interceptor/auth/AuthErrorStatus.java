package com.heylocal.traveler.interceptor.auth;

public enum AuthErrorStatus {
  NO_HTTP_HEADER_VALUE("Authorization HTTP 헤더에 값이 없습니다."),
  NOT_STARTS_WITH_BEARER("Authorization HTTP 헤더의 값은 Bearer 로 시작해야 합니다."),
  EXPIRED_TOKEN("토큰 유효기간이 만료되었습니다."),
  BAD_TOKEN("토큰 값이 잘못되었습니다.");

  private String value;

  AuthErrorStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
