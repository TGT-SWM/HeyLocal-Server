/**
 * packageName    : com.heylocal.traveler.exception
 * fileName       : NotFoundException
 * author         : 우태균
 * date           : 2022/09/10
 * description    : 404 NotFound 관련 예외
 */

package com.heylocal.traveler.exception;

import com.heylocal.traveler.exception.code.ErrorCode;

public class NotFoundException extends AllException {
	public NotFoundException(ErrorCode code) {
		super(code);
	}
	public NotFoundException(ErrorCode code, String description) {
		super(code, description);
	}
}
