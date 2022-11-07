package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.HomeApi;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController implements HomeApi {
  @Override
  public String home() {
    return "index";
  }
}
