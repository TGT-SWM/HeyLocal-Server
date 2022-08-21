package com.heylocal.traveler.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

public class AuthTokenDto {
  @Getter
  @Setter
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TokenPairRequest {
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String refreshToken;
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TokenPairResponse {
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String refreshToken;
  }
}
