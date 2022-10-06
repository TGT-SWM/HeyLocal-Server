package com.heylocal.traveler.controller.advice;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * packageName    : com.heylocal.traveler.controller.advice
 * fileName       : ControllerAdvice
 * author         : 우태균
 * date           : 2022/08/13
 * description    : 에러 처리 ControllerAdvice
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

  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessageResponse forbiddenException(ForbiddenException ex) {
    ErrorMessageResponse message = new ErrorMessageResponse(ex);
    return message;
  }
}
