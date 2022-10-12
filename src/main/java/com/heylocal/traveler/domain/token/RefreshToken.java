/**
 * packageName    : com.heylocal.traveler.domain.token
 * fileName       : RefreshToken
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 사용자 인가 Refresh Token 엔티티
 */

package com.heylocal.traveler.domain.token;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RefreshToken extends BaseTimeEntity {
  @Id
  @GeneratedValue
  private long id;
  @Column(length = 510, nullable = false)
  private String tokenValue;
  @Column(nullable = false)
  private LocalDateTime expiredDateTime;
  @OneToOne(fetch = FetchType.LAZY)
  private User user;

  //양방향 설정
  @OneToOne(mappedBy = "refreshToken", cascade = CascadeType.ALL)
  private AccessToken accessToken;

  public void associateAccessToken(AccessToken accessToken) {
    this.accessToken = accessToken;
    if (accessToken.getRefreshToken() != this) {
      accessToken.associateRefreshToken(this);
    }
  }

  public void associateUser(User user) {
    this.user = user;
    if (user.getRefreshToken() != this) {
      user.registerRefreshToken(this);
    }
  }

  public void updateTokenValue(String newTokenValue) {
    this.tokenValue = newTokenValue;
  }
}
