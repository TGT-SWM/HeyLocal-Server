package com.heylocal.traveler.domain.plan;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 특정 일자 (하루) 스케줄표
 */
@Entity
@Table(name = "DAY_SCHEDULE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class DaySchedule extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private LocalDate dateTime; //스케줄 날짜

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Plan plan;

  //양방향 설정

  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
  private List<PlaceItem> placeItemList = new ArrayList<>();
}
