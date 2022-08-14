package com.heylocal.traveler.controller.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends Exception {
	public NotFoundException(String message) {
		super(message);
	}
}
