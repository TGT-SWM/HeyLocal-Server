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
