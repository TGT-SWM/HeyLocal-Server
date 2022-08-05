package com.heylocal.traveler.domain.travel;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.chat.ChatRoom;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.user.Manager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 여행
 */

@Entity
@Table(name = "TRAVEL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Travel extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TravelStatus status;

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean isFixed;

  @OneToOne
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Manager manager;

  @OneToOne
  @JoinColumn(nullable = false)
  private ChatRoom chatRoom;

}
