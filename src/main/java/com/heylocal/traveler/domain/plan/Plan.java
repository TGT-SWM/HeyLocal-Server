package com.heylocal.traveler.domain.plan;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행
 */

@Entity
@Table(name = "PLAN")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Plan extends BaseTimeEntity {
  @Id
  @GeneratedValue
  private Long id;

  // 별도로 저장하고 있는 것보다. 여행 날짜를 통해 유도하는 것은 어떨까?
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlanStatus status;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  @ManyToOne(optional = false)
  private User user;

  //양방향 설정

  @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<DaySchedule> dayScheduleList = new ArrayList<>();
}
