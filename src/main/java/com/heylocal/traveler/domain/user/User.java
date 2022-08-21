package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.notification.Notification;
import com.heylocal.traveler.domain.order.OrderRequest;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.token.AccessToken;
import com.heylocal.traveler.domain.token.RefreshToken;
import com.heylocal.traveler.domain.userreview.UserReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자(여행자, 매니저, 서비스관리자) 부모 클래스
 */

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
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

  @Column(length = 13, nullable = false, unique = true)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserType userType;

  //양방향 설정

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Notification> notificationList = new ArrayList<>();

  @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
  private List<OrderRequest> sentOrderRequestList = new ArrayList<>(); //보낸 요청 리스트

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<OrderRequest> receivedOrderRequestList = new ArrayList<>(); //받은 요청 리스트

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile userProfile;

  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
  private List<UserReview> userReview = new ArrayList<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private RefreshToken refreshToken;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private AccessToken accessToken;
}
