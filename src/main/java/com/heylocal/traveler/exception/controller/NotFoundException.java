package com.heylocal.traveler.exception.controller;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

public class NotFoundException extends AllException {
	public NotFoundException(ErrorCode code) {
		super(code);
	}
}
