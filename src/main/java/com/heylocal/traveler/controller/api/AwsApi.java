package com.heylocal.traveler.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : AwsApi
 * author         : 우태균
 * date           : 2022/09/23
 * description    : AWS S3 접근을 위한 API 인터페이스
 */

@RequestMapping("/aws/sns")
public interface AwsApi {
  @Operation(summary = "답변 이미지 파일이 S3에 저장되었을 때, AWS가 호출하는 Callback API", description = "AWS의 Simple Notification Service가 호출한다.", tags = {"Aws"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공")
  })
  @PostMapping(value = "/opinion/img/put", consumes = "text/plain")
  void saveOpinionImgMessage(
      @Parameter(in = ParameterIn.DEFAULT, description = "Put된 S3 Object 정보", required = true) @RequestBody String request
  ) throws Exception;

  @Operation(summary = "답변 이미지 파일이 S3에서 제거되었을 때, AWS가 호출하는 Callback API", description = "AWS의 Simple Notification Service가 호출한다.", tags = {"Aws"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공")
  })
  @PostMapping(value = "/opinion/img/delete", consumes = "text/plain")
  void deleteOpinionImgMessage(
      @Parameter(in = ParameterIn.DEFAULT, description = "Delete된 S3 Object 정보", required = true) @RequestBody String request
  ) throws Exception;

  @Operation(summary = "프로필 이미지 파일이 S3에 저장되었을 때, AWS가 호출하는 Callback API", description = "AWS의 Simple Notification Service가 호출한다.", tags = {"Aws"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공")
  })
  @PostMapping(value = "/profile/img/put", consumes = "text/plain")
  void saveProfileImgMessage(
      @Parameter(in = ParameterIn.DEFAULT, description = "Put된 S3 Object 정보", required = true) @RequestBody String request
  ) throws Exception;

  @Operation(summary = "프로필 이미지 파일이 S3에서 제거되었을 때, AWS가 호출하는 Callback API", description = "AWS의 Simple Notification Service가 호출한다.", tags = {"Aws"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "호출 성공")
  })
  @PostMapping(value = "/profile/img/delete", consumes = "text/plain")
  void deleteProfileImgMessage(
      @Parameter(in = ParameterIn.DEFAULT, description = "Delete된 S3 Object 정보", required = true) @RequestBody String request
  ) throws Exception;
}
