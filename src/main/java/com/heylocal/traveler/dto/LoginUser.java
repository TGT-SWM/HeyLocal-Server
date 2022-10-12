/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : LoginUser
 * author         : 우태균
 * date           : 2022/08/20
 * description    : 로그인된 사용자 관련 DTO
 */

package com.heylocal.traveler.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
  private long id;
}
