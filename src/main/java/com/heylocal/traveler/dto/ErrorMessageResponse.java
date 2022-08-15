package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ErrorMessageResponse {
  private String reason;
  private LocalDateTime dateTime;

  public ErrorMessageResponse() {
    this.reason = "";
    this.dateTime = LocalDateTime.now();
  }

  public ErrorMessageResponse(String reason) {
    this.reason = reason;
    this.dateTime = LocalDateTime.now();
  }

  @Schema(example = "오류가 발생한 이유", description = "오류 발생 이유")
  public String getReason() {
    return reason;
  }

  @Schema(example = "2022-08-13T15:03:45.45704", description = "오류 발생 시각")
  public LocalDateTime getDateTime() {
    return dateTime;
  }
}
