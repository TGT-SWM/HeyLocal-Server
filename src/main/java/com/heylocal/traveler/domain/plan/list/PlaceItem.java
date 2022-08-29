package com.heylocal.traveler.domain.plan.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.DaySchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 스케줄표에 들어갈 항목(장소+순서)
 * 중간 연결 엔티티
 */

@Entity
@Table(name = "PLACE_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class PlaceItem extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private DaySchedule schedule;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceItemType type; // 원장소/대체장소 구분

  @ManyToOne(optional = false)
  private Place place;

  @Column(nullable = false)
  private Integer itemIndex; //오름차순

  private Long originalPlaceId; // 현재 대체장소일때, 원장소의 id
}
