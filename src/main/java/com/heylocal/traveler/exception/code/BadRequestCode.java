/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : BadRequestCode
 * author         : 우태균
 * date           : 2022/09/18
 * description    : http 요청 데이터 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

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
