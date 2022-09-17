package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * Http 요청 데이터 자체 관련 오류 코드
 * <pre/>
 */
public enum BadRequestCode implements ErrorCode {
  BAD_INPUT_FORM("입력 값의 형식이 올바르지 않습니다."),

  ALREADY_EXISTS("이미 정보가 존재합니다.");

  private String description;

  BadRequestCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
