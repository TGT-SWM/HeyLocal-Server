package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @GetMapping("/test/auth")
  public LoginUser get(LoginUser loginUser) {
    return loginUser;
  }
}
