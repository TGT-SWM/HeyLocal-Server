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

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceTasteType placeTasteType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ActivityTasteType activityTasteType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SnsTasteType snsTasteType;

}