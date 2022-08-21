package com.heylocal.traveler.controller.exception;

import lombok.NoArgsConstructor;

/**
 * <pre>
 * 404 Not Found 관련 예외
 * </pre>
 */
@NoArgsConstructor
public class NotFoundException extends Exception {
	public NotFoundException(String message) {
		super(message);
	}
}
