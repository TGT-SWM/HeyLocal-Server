package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.user.UserType;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
  private long id;
  private String accountId;
  private String nickname;
  private String phoneNumber;
  private UserType userType;
}
