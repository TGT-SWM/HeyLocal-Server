/**
 * packageName    : com.heylocal.traveler.domain.plan
 * fileName       : DaySchedule
 * author         : 우태균
 * date           : 2022/09/26
 * description    : 여행 계획표의 하루 스케줄 엔티티
 */

package com.heylocal.traveler.domain.plan;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DAY_SCHEDULE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DaySchedule extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private LocalDate dateTime; //스케줄 날짜

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Plan plan;

  //양방향 설정

  @Builder.Default
  @OrderBy(value = "itemIndex ASC")
  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
  private List<PlaceItem> placeItemList = new ArrayList<>();

  /**
   * <pre>
   * 해당 PlaceItem을 추가합니다.
   * @param placeItem PlaceItem 엔티티
   * </pre>
   */
  public void addPlaceItem(PlaceItem placeItem) {
    placeItemList.add(placeItem);
    if (placeItem.getSchedule() != this)
      placeItem.registerAt(this);
  }

  public void register(Plan plan) {
    this.plan = plan;
    if (!plan.getDayScheduleList().contains(this))
      plan.addDaySchedule(this);
  }
}
