package com.heylocal.traveler.dto.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

public class AwsSnsDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @Builder
  public static class AwsSnsRequest {
    private S3ObjectDto object;
    private LocalDateTime eventTime;
  }
}
