package com.heylocal.traveler.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

public class SignupDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UserInfoCheckResponse {
    private boolean isAlreadyExist;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignupRequest {
    /*
     * 이 DTO 클래스에 한정하여,
     * 값 Validation 은 컨트롤러에서 직접 수행한다. (empty 검증 제외)
     * 왜냐하면 @Value 를 통해, properties 에서 검증용 패턴을 얻어올 수 없기 때문이다.
     */
    @NotEmpty
    private String accountId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String phoneNumber;
  }
}
