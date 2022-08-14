package com.heylocal.traveler.domain.userreview;

import com.heylocal.traveler.domain.StarScore;
import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 매니저 리뷰
 */

@Entity
@Table(name = "MANAGER_REVIEW")
@DiscriminatorValue("MANAGER-REVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ManagerReview extends UserReview {
  @OneToOne
  @JoinColumn(nullable = false)
  private Travel travel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StarScore kindness;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StarScore responsiveness;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StarScore noteDetail;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StarScore notePrecision;

  private String otherOpinion;
}
