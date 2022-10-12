/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : ForbiddenCode
 * author         : 우태균
 * date           : 2022/09/07
 * description    : 접근 권한 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

public enum ForbiddenCode implements ErrorCode {
  NO_PERMISSION("접근 권한이 없습니다.");

  private String description;

  ForbiddenCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
