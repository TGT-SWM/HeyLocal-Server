package com.heylocal.traveler.domain.order.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.order.PrecautionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 의뢰서 작성 내용 - 주의사항 목록
 */

@Entity
@Table(name = "PRECAUTION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Precaution extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @Enumerated(EnumType.STRING)
  private PrecautionType type;

  private String precautionInfo; //기타 주의사항 세부 내용
}
