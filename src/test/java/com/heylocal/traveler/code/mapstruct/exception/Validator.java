package com.heylocal.traveler.code.mapstruct.exception;

public class Validator {

  public long validate1(Long id) {
    if (id < 0) {
      throw new IllegalArgumentException();
    }
    return id;
  }

  public String validate2(String field) {
    if (field == null || field.length() == 0) {
      throw new IllegalArgumentException();
    }
    return field;
  }

}
