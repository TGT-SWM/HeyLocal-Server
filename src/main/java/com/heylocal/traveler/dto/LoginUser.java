package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.user.UserRole;
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
  private UserRole userRole;
}
