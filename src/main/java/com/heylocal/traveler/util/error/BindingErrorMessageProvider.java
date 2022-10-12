/**
 * packageName    : com.heylocal.traveler.util.error
 * fileName       : BindingErrorMessageProvider
 * author         : 우태균
 * date           : 2022/08/31
 * description    : 컨트롤러의 BindingError의 에러 메시지 처리 클래스
 */

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
