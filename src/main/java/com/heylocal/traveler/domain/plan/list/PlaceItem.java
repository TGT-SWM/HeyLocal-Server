/**
 * packageName    : com.heylocal.traveler.domain.plan.list
 * fileName       : PlaceItem
 * author         : 우태균
 * date           : 2022/10/06
 * description    : 여행 일정표의 방문 장소 엔티티
 */

package com.heylocal.traveler.domain.plan.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * 스케줄표에 들어갈 항목(장소+순서)
 * 중간 연결 엔티티
 */

@Entity
@Table(name = "PLACE_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PlaceItem extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private DaySchedule schedule;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceItemType type; // 원장소/대체장소 구분

  @ManyToOne(optional = false)
  private Place place;

  @Column(nullable = false)
  private Integer itemIndex; //오름차순

  private Long originalPlaceId; // 현재 대체장소일때, 원장소의 id

  private LocalTime arrivalTime; // 도착 시간

  @ManyToOne
  private Opinion opinion;

  /**
   * <pre>
   * 해당 DaySchedule에 PlaceItem을 추가합니다.
   * @param daySchedule 스케줄 엔티티
   * </pre>
   */
  public void registerAt(DaySchedule daySchedule) {
    this.schedule = daySchedule;
    if (!daySchedule.getPlaceItemList().contains(this)) // O(N) 시간 소요 문제
      daySchedule.addPlaceItem(this);
  }

  public void registerOpinion(Opinion opinion) {
    this.opinion = opinion;
    if (!opinion.getPlaceItemList().contains(this)) {
      opinion.registerPlaceItem(this);
    }
  }
}
