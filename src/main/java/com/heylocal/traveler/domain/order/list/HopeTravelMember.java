package com.heylocal.traveler.domain.order.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.order.OrderSheet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 의뢰서 작성 내용 - 여행 동행인 목록
 */

@Entity
@Table(name = "HOPE_TRAVEL_MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HopeTravelMember extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private Age age;

  public enum Gender {
    MAN, WOMAN
  }
  public enum Age {
    UNDER_10, UNDER_20, UNDER_30, UNDER_40, UNDER_50, UNDER_60, UNDER_70, UNDER_80, UNDER_90
  }
}
