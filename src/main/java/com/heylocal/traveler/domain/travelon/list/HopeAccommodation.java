package com.heylocal.traveler.domain.travelon.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 희망 숙소 유형 목록
 */

@Entity
@Table(name = "HOPE_ACCOMMODATION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class HopeAccommodation extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccommodationType type;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

}
