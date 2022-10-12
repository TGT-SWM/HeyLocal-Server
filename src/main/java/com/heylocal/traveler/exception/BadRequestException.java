/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : BadRequestException
 * author         : 우태균
 * date           : 2022/09/10
 * description    : 400 Bad Request 관련 예외
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class BadRequestException extends AllException {

  public BadRequestException(ErrorCode code) {
    super(code);
  }

  public BadRequestException(ErrorCode code, String description) {
    super(code, description);
  }
}
