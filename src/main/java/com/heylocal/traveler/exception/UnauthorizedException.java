/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : UnauthorizedException
 * author         : 우태균
 * date           : 2022/09/10
 * description    : 401 Unauthorized 관련 예외
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class UnauthorizedException extends AllException {
  public UnauthorizedException(ErrorCode code) {
    super(code);
  }
  public UnauthorizedException(ErrorCode code, String description) {
    super(code, description);
  }
}
