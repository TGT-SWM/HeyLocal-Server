package com.heylocal.traveler.domain.order;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 매칭 요청
 */

@Entity
@Table(name = "ORDER_REQUEST")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class OrderRequest extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User requester;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User receiver;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @Enumerated
  @Column(nullable = false)
  private OrderRequestStatus status;
}
