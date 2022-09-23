package com.heylocal.traveler.dto.aws;

import lombok.*;

import java.time.LocalDateTime;

public class AwsSnsDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AwsSnsRequest {
    private S3ObjectDto object;
    private LocalDateTime eventTime;
  }
}
