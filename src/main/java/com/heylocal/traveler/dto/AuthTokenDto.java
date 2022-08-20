package com.heylocal.traveler.dto;

import lombok.*;

public class AuthTokenDto {
  @Getter
  @Setter
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TokenPairRequest {
    private String accessToken;
    private String refreshToken;
  }
}
