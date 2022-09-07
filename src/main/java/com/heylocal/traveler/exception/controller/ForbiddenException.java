package com.heylocal.traveler.exception.controller;

import com.heylocal.traveler.exception.AllException;
import com.heylocal.traveler.exception.code.ErrorCode;

public class ForbiddenException extends AllException {
  public ForbiddenException(ErrorCode code) {
    super(code);
  }
  public ForbiddenException(ErrorCode code, String description) {
    super(code, description);
  }
}
