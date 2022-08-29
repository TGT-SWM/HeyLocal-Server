package com.heylocal.traveler.exception.controller;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 401 Unauthorized 관련 예외
 * </pre>
 */
public class UnauthorizedException extends AllException {
  public UnauthorizedException(ErrorCode code) {
    super(code);
  }
  public UnauthorizedException(ErrorCode code, String description) {
    super(code, description);
  }
}
