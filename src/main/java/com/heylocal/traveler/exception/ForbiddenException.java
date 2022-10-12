/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : ForbiddenException
 * author         : 우태균
 * date           : 2022/09/10
 * description    : 403 Forbidden 관련 예외
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class ForbiddenException extends AllException {
  public ForbiddenException(ErrorCode code) {
    super(code);
  }
  public ForbiddenException(ErrorCode code, String description) {
    super(code, description);
  }
}
