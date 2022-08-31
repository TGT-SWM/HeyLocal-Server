package com.heylocal.traveler.exception.controller;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 400 Bad Request 관련 예외
 * </pre>
 */
public class BadRequestException extends AllException {

  public BadRequestException(ErrorCode code) {
    super(code);
  }

  public BadRequestException(ErrorCode code, String description) {
    super(code, description);
  }
}
