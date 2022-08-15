package com.heylocal.traveler.controller.advice;

import com.heylocal.traveler.controller.exception.BadRequestException;
import com.heylocal.traveler.controller.exception.NotFoundException;
import com.heylocal.traveler.dto.ErrorMessageResponse;
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
  public ErrorMessageResponse resourceNotFoundException(BadRequestException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex.getMessage());
    return message;
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorMessageResponse notFoundException(NotFoundException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex.getMessage());
    return message;
  }
}
