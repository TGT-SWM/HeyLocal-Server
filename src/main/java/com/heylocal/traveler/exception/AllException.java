/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : AllException
 * author         : 우태균
 * date           : 2022/08/31
 * description    : 모든 커스텀 예외 클래스가 상속받아야 하는 클래스
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class AllException extends Exception {
  private ErrorCode code;
  private String description;

  public AllException(ErrorCode code) {
    super(code.getDescription());
    this.code = code;
    this.description = code.getDescription();
  }

  public AllException(ErrorCode code, String description) {
    super(description);
    this.code = code;
    this.description = description;
  }

  public ErrorCode getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }
}
