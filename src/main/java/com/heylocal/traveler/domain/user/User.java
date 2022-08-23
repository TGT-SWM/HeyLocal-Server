package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 사용자 & 관리자 클래스
 */

@Entity
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class User extends BaseTimeEntity {
  @Id @GeneratedValue
  private long id;

  @Column(length = 20, nullable = false, unique = true)
  private String accountId;

  @Column(nullable = false)
  private String password;

  @Column(length = 20, nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole userRole;

  //양방향 설정



  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private RefreshToken refreshToken;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private AccessToken accessToken;

  public void registerRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
    if (refreshToken.getUser() != this) {
      refreshToken.associateUser(this);
    }
  }

  public void registerAccessToken(AccessToken accessToken) {
    this.accessToken = accessToken;
    if (accessToken.getUser() != this) {
      accessToken.associateUser(this);
    }
  }
}
