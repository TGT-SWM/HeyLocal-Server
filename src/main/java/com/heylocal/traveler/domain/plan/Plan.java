package com.heylocal.traveler.domain.plan;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  @OneToOne(fetch = FetchType.LAZY)
  private TravelOn travelOn;

  @ManyToOne(optional = false)
  private User user;

  //양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<DaySchedule> dayScheduleList = new ArrayList<>();

  public void releaseTravelOn() {
    TravelOn temp = this.travelOn;
    this.travelOn = null;

    if (!Objects.isNull(temp)) {
      temp.releasePlan();
    }
  }
}
