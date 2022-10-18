/**
 * packageName    : com.heylocal.traveler.domain.user
 * fileName       : User
 * author         : 우태균
 * date           : 2022/10/01
 * description    : 서비스 사용자 엔티티
 */

package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.notification.Notification;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

  public void setUserProfile(UserProfile profile) {
    this.userProfile = profile;
    if (userProfile.getUser() != this) {
      userProfile.setUser(this);
    }
  }

  public void addOpinion(Opinion opinion) {
    this.opinionList.add(opinion);
    if (opinion.getAuthor() != this) {
      opinion.setAuthor(this);
    }
  }

  public void removeOpinion(Opinion target) {
    this.opinionList.remove(target);
  }
}
