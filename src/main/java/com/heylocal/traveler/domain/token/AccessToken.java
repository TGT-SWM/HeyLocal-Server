package com.heylocal.traveler.domain.token;

import com.heylocal.traveler.domain.BaseTimeEntity;
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
  private String tokenValue;
  private LocalDateTime expiredDateTime;

  //양방향 설정
  @OneToOne(mappedBy = "accessToken", cascade = CascadeType.ALL)
  private RefreshToken refreshToken;

  public void associateRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;

    if (refreshToken.getAccessToken() != this) {
      refreshToken.associateAccessToken(this);
    }
  }
}
