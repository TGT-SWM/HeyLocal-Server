package com.heylocal.traveler.domain.userreview;

import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 여행자 의견 남기기
 */

@Entity
@Table(name = "TRAVELER_REVIEW")
@DiscriminatorValue("TRAVELER-REVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class TravelerReview extends UserReview {
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Travel travel;

  private String message;
}
