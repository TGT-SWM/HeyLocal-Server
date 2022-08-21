package com.heylocal.traveler.exception.service;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

/**
 * <pre>
 * 클라이언트로부터 받은 input 값이 올바르지 않을 경우,
 * 발생시키는 예외
 * <pre/>
 */
public class BadArgumentException extends AllException {
  public BadArgumentException(ErrorCode code) {
    super(code);
  }
}
