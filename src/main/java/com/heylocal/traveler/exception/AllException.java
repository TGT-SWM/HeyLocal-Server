package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 컨트롤러에서 뱉는 모든 예외는 이 클래스를 상속받아야 함
 * <pre/>
 */
public class AllException extends Exception {
  private ErrorCode code;

  public AllException(ErrorCode code) {
    super(code.getDescription());
    this.code = code;
  }

  public ErrorCode getCode() {
    return this.code;
  }
}
