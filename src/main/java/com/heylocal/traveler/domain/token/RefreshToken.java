package com.heylocal.traveler.domain.token;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Refresh Token 엔티티
 */
@Entity
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class RefreshToken extends BaseTimeEntity {
  @Id
  @GeneratedValue
  private long id;
  private String tokenValue;
  private LocalDateTime expiredDateTime;

  @OneToOne
  @JoinColumn(nullable = false)
  private AccessToken accessToken;

  public void associateAccessToken(AccessToken accessToken) {
    this.accessToken = accessToken;
    if (accessToken.getRefreshToken() != this) {
      accessToken.associateRefreshToken(this);
    }
  }
}
