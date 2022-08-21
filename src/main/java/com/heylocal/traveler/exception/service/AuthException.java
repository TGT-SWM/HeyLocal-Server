package com.heylocal.traveler.exception.service;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 인가 관련 예외
 * </pre>
 */
public class AuthException extends AllException {
  public AuthException(ErrorCode code) {
    super(code);
  }
}
