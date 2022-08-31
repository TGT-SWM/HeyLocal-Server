package com.heylocal.traveler.exception.service;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 클라이언트로부터 받은 **인증 관련 input 값** 이 올바르지 않을 경우,
 * 발생시키는 예외
 * <pre/>
 */
public class SigninArgumentException extends AllException {
  public SigninArgumentException(ErrorCode code) {
    super(code);
  }
  public SigninArgumentException(ErrorCode code, String description) {
    super(code, description);
  }
}
