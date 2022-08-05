package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 사용자(여행자, 매니저, 서비스관리자) 부모 클래스
 * TODO https://swm13-tgt.atlassian.net/browse/S3T-298 반영해야함
 */

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {
  @Id @GeneratedValue
  private long id;

  @Column(length = 20, nullable = false, unique = true)
  private String accountId;

  @Column(length = 50, nullable = false)
  private String password;

  @Column(length = 13, nullable = false, unique = true)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserType userType;
}
