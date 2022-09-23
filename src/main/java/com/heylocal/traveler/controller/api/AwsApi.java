package com.heylocal.traveler.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heylocal.traveler.dto.aws.AwsSnsDto;
import com.heylocal.traveler.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/aws/sns")
public interface AwsApi {
  @Operation(summary = "답변 이미지 파일이 S3에 저장되었을 때, AWS가 호출하는 Callback API", description = "AWS의 Simple Notification Service가 호출한다.", tags = {"Aws"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공")
  })
  @PostMapping(value = "/opinion/img", consumes = "text/plain")
  void postSavedOpinionImgMessage(
      @Parameter(in = ParameterIn.DEFAULT, description = "Put된 S3 Object 정보", required = true) @RequestBody String request
      ) throws NotFoundException, JsonProcessingException;
}
