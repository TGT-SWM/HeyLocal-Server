package com.heylocal.traveler.exception.service;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 작업을 할 수 없는 경우 발생시키는 예외
 * </pre>
 */
public class TaskRejectException extends AllException {
  public TaskRejectException(ErrorCode code) {
    super(code);
  }
  public TaskRejectException(ErrorCode code, String description) {
    super(code, description);
  }
}
