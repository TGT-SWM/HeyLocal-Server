package com.heylocal.traveler.domain.travelon.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 여행 동행인 목록
 */
@Entity
@Table(name = "TRAVEL_MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class TravelMember extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MemberType memberType;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  public void registerAt(TravelOn travelOn) {
    this.travelOn = travelOn;
    if (!travelOn.getTravelMemberSet().contains(this)) {
      travelOn.getTravelMemberSet().add(this);
    }
  }
}
