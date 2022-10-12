/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : NotFoundCode
 * author         : 우태균
 * date           : 2022/08/21
 * description    : Not Found 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

public enum NotFoundCode implements ErrorCode {
  NOT_FOUND_URL("존재하지 않는 URL입니다."),
  NO_INFO("존재하지 않는 정보입니다.");

  private String description;

  NotFoundCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
