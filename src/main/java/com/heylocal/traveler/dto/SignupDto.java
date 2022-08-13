package com.heylocal.traveler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SignupDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserInfoCheckResponse {
    private boolean isAlreadyExist;
  }
}
