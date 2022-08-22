package com.heylocal.traveler.domain.payment;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 현지머니 거래 내역
 * Travel에 있는 User 정보를 가지고 사용/획득을 구분하면 됨
 */

@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Payment extends BaseTimeEntity {
  @Id @GeneratedValue
  private long id;

  @Enumerated(EnumType.STRING)
  private PaymentType type;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = true)
  private Travel travel; //만약 단순 포인트 충전이라면 null

  private Integer point; //사용·획득한 현지머니
}
