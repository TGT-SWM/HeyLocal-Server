package com.heylocal.traveler.controller.exception;

import lombok.NoArgsConstructor;

/**
 * <pre>
 * 401 Unauthorized 관련 예외
 * </pre>
 */
@NoArgsConstructor
public class UnauthorizedException extends Exception {
  public UnauthorizedException(String message) {
    super(message);
  }
}
