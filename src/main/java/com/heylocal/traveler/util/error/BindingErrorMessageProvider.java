package com.heylocal.traveler.util.error;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class BindingErrorMessageProvider {

  public String getFieldErrMsg(BindingResult bindingResult) {
    String errMsg = bindingResult.getFieldError().getField() + " 필드가 " + bindingResult.getFieldError().getDefaultMessage();
    return errMsg;
  }

}
