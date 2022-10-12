/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : AuthTokenDto
 * author         : 우태균
 * date           : 2022/08/21
 * description    : 인증·인가 토큰 관련 DTO
 */

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
    private String accessToken;
    private String refreshToken;
  }
}
