package com.heylocal.traveler.domain.travelon;

import com.heylocal.traveler.domain.BaseTimeEntity;

import javax.persistence.*;

/**
 * 여행 성향 모음
 */
@Entity
@Table(name = "TRAVEL_TYPE_GROUP")
public class TravelTypeGroup extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private TravelOn travelOn;

  @Enumerated(EnumType.STRING)
  private PlaceTasteType placeTasteType;

  @Enumerated(EnumType.STRING)
  private ActivityTasteType activityTasteType;

  @Enumerated(EnumType.STRING)
  private SnsTasteType snsTasteType;

}