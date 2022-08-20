package com.heylocal.traveler.dto;

import com.heylocal.traveler.exception.code.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ErrorMessageResponse {
  private ErrorCode code;
  private String description;
  private LocalDateTime dateTime;

  public ErrorMessageResponse() {
    this.description = "";
    this.dateTime = LocalDateTime.now();
  }

  public ErrorMessageResponse(ErrorCode code) {
    this.code = code;
    this.description = code.getDescription();
    this.dateTime = LocalDateTime.now();
  }

  @Schema(example = "코드", description = "오류 코드")
  public ErrorCode getCode() {
    return code;
  }

  @Schema(example = "오류가 발생한 이유", description = "오류 발생 이유")
  public String getDescription() {
    return description;
  }

  @Schema(example = "2022-08-13T15:03:45.45704", description = "오류 발생 시각")
  public LocalDateTime getDateTime() {
    return dateTime;
  }
}
