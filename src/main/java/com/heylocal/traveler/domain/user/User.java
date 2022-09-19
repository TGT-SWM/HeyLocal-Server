package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.notification.Notification;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 & 관리자 클래스
 */

@Entity
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

  @Builder.Default
  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Notification> notificationList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Opinion> opinionList = new ArrayList<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private UserProfile userProfile;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Plan> planList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<TravelOn> travelOnList = new ArrayList<>();

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

  public void releaseAllTokens() {
    this.refreshToken = null;
    this.accessToken = null;
  }

  public void registerUserProfile(UserProfile profile) {
    this.userProfile = profile;
    if (userProfile.getUser() != this) {
      userProfile.associateUser(this);
    }
  }

  public void addOpinion(Opinion opinion) {
    this.opinionList.add(opinion);
    if (opinion.getAuthor() != this) {
      opinion.setAuthor(this);
    }
  }
}
