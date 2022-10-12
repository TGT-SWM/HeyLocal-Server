/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : TokenException
 * author         : 우태균
 * date           : 2022/09/10
 * description    : 인가·인증 토큰 관련 예외
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class TokenException extends AllException {
  public TokenException(ErrorCode code) {
    super(code);
  }
  public TokenException(ErrorCode code, String description) {
    super(code, description);
  }
}
