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

    private String type;
    private String messageId;
    private String token;
    private String subscribeURL;
    private String signature;
    private String signingCertURL;
  }
}
