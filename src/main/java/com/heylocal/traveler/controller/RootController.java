/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : RootController
 * author         : 우태균
 * date           : 2022/10/13
 * description    : 홈(Root) API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RootApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class RootController implements RootApi {
  @Override
  public String root() {
    return "redirect:/home";
  }
}
