package com.heylocal.traveler.domain.order.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.order.AccommodationType;
import com.heylocal.traveler.domain.order.OrderSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 의뢰서 작성 내용 - 희망 숙소 유형 목록
 */

@Entity
@Table(name = "HOPE_ACCOMMODATION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HopeAccommodation extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @Enumerated(EnumType.STRING)
  private AccommodationType type;

  private String info; //숙소 타입이 ETC 인 경우가 아니면 null
}
