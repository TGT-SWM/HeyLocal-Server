/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : SigninDto
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 로그인 관련 DTO
 */

package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.user.UserRole;
import lombok.*;

import javax.validation.constraints.NotEmpty;

public class SigninDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SigninRequest {
    @NotEmpty
    private String accountId;
    @NotEmpty
    private String password;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SigninResponse {
    private long id;
    private String accountId;
    private String nickname;
    private UserRole userRole;
    private String accessToken;
    private String refreshToken;
  }
}
