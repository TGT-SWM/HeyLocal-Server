package com.heylocal.traveler.domain.travel;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.chat.ChatRoom;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.payment.Payment;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.userreview.ManagerReview;
import com.heylocal.traveler.domain.userreview.TravelerReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행
 */

@Entity
@Table(name = "TRAVEL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Travel extends BaseTimeEntity {
  @Id
  @GeneratedValue
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

  //양방향 설정

  @OneToOne(mappedBy = "travel", cascade = CascadeType.ALL)
  private ChatRoom chatRoom;

  @OneToOne(mappedBy = "travel", cascade = CascadeType.ALL)
  private Payment payment;

  @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
  private List<DaySchedule> dayScheduleList = new ArrayList<>();

  @OneToOne(mappedBy = "travel", cascade = CascadeType.ALL)
  private ManagerReview managerReview;

  @OneToOne(mappedBy = "travel", cascade = CascadeType.ALL)
  private TravelerReview travelerReview;
}
