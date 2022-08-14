package com.heylocal.traveler.domain.profile;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 사용자(여행자, 매니저) 프로필 부모 클래스
 */

@Entity
@Table(name = "USER_PROFILE")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class UserProfile extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @OneToOne
  @JoinColumn(nullable = false)
  private User user;

  @ColumnDefault("0")
  private Integer possessionPoint; //보유 포인트
}
