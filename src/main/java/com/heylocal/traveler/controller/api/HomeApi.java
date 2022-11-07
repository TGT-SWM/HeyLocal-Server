package com.heylocal.traveler.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/home")
public interface HomeApi {
  @Operation(tags = {"Home"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공"),
  })
  @GetMapping()
  String home();
}
