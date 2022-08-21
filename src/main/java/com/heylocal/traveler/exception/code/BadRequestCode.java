package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * Http 요청 데이터 자체 관련 오류 코드
 * <pre/>
 */
public enum BadRequestCode implements ErrorCode {
  EMPTY_FIELD("필드는 비어있을 수 없습니다.");

  private String description;

  BadRequestCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
