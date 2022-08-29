package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 모든 예외는 이 클래스를 상속받아야 함
 * <pre/>
 */
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
}
