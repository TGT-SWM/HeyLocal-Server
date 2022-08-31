package com.heylocal.traveler.controller.advice;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.exception.controller.UnauthorizedException;
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
  public ErrorMessageResponse badRequestException(BadRequestException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex);
    return message;
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ErrorMessageResponse unauthorizedException(UnauthorizedException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex);
    return message;
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessageResponse notFoundException(NotFoundException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex);
    return message;
  }
}
