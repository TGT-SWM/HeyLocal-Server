package com.heylocal.traveler.controller.exception;

import lombok.NoArgsConstructor;

/**
 * <pre>
 * 400 Bad Rquest 관련 예외
 * </pre>
 */
@NoArgsConstructor
public class BadRequestException extends Exception {

  public BadRequestException(String message) {
    super(message);
  }

}
