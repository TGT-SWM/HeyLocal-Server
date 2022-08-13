package com.heylocal.traveler.controller.advice;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * <pre>
 * 에러 응답 클래스
 * </pre>
 */
@Getter
public class ErrorMessage {
  private String reason;
  private LocalDateTime dateTime;

  protected ErrorMessage() {
    this.reason = "";
    this.dateTime = LocalDateTime.now();
  }

  protected ErrorMessage(String reason) {
    this.reason = reason;
    this.dateTime = LocalDateTime.now();
  }
}
