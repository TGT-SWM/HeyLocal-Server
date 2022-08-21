package com.heylocal.traveler.domain.token;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Access Token 엔티티
 */
@Entity
@Table(name = "ACCESS_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class AccessToken extends BaseTimeEntity {
  @Id @GeneratedValue
  private long id;
  @Column(length = 510, nullable = false)
  private String tokenValue;
  @Column(nullable = false)
  private LocalDateTime expiredDateTime;
  @OneToOne
  private RefreshToken refreshToken;
  @OneToOne
  private User user;

  public void associateRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;

    if (refreshToken.getAccessToken() != this) {
      refreshToken.associateAccessToken(this);
    }
  }

  public void updateTokenValue(String newTokenValue) {
    this.tokenValue = newTokenValue;
  }
}
