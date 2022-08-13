package com.heylocal.traveler.controller.advice;

import com.heylocal.traveler.controller.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <pre>
 * 에러 처리 ControllerAdvice
 * </pre>
 */
@RestControllerAdvice
public class ControllerAdvice {
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorMessage resourceNotFoundException(BadRequestException ex) {
    ErrorMessage message = new ErrorMessage(ex.getMessage());
    return message;
  }
}
