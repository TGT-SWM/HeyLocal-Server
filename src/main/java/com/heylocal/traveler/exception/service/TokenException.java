package com.heylocal.traveler.exception.service;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 토큰 관련 예외
 * <pre/>
 */
public class TokenException extends AllException {
  public TokenException(ErrorCode code) {
    super(code);
  }
  public TokenException(ErrorCode code, String description) {
    super(code, description);
  }
}
