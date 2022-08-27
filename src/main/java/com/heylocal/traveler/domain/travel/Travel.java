package com.heylocal.traveler.domain.travel;

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
@Table(name = "TRAVEL")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
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

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  @ManyToOne(optional = false)
  private User user;

  //양방향 설정

  @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<DaySchedule> dayScheduleList = new ArrayList<>();
}
