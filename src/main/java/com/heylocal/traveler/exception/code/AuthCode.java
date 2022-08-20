package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 인가 관련 오류 코드
 * </pre>
 */
public enum AuthCode implements ErrorCode {
  NOT_EXIST_REFRESH_TOKEN("존재하지 않는 Refresh Token 입니다."),
  EXPIRED_REFRESH_TOKEN("만료된 Refresh Token 입니다."),
  NOT_EXPIRED_ACCESS_TOKEN("아직 Access Token 이 만료되지 않았습니다."),
  NOT_MATCH_PAIR("Access Token과 Refresh Token이 서로 매치되지 않습니다.")
  ;

  private String description;

  AuthCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
