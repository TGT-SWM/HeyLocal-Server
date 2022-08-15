package com.heylocal.traveler.service.exception;

import com.heylocal.traveler.controller.exception.BadRequestException;
import lombok.NoArgsConstructor;

/**
 * 클라이언트로부터 받은 input 값이 올바르지 않을 경우,
 * 발생시키는 예외
 */
@NoArgsConstructor
public class BadArgumentException extends BadRequestException {
  public BadArgumentException(String message) {
    super(message);
  }
}
