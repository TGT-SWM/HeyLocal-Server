package com.heylocal.traveler.domain.userreview;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 사용자(여행자, 매니저) 리뷰 부모 클래스
 */

@Entity
@Table(name = "USER_REVIEW")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserReview extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User writer;
}
