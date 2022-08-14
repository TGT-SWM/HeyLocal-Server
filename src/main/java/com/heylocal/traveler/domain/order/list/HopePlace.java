package com.heylocal.traveler.domain.order.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.place.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 의뢰서 작성 내용 - 희망 방문장소 목록
 * 중간 연결 엔티티
 */

@Entity
@Table(name = "HOPE_PLACE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HopePlace extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Place place;
}
