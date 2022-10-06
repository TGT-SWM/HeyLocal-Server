package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : TestController
 * author         : 우태균
 * date           : 2022/08/20
 * description    : 테스트용 컨트롤러
 */

@RestController
public class TestController {
  @GetMapping("/test/auth")
  public LoginUser get(LoginUser loginUser) {
    return loginUser;
  }
}
