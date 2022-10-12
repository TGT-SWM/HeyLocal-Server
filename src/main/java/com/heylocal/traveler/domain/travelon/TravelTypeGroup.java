/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : TravelTypeGroup
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 여행On 질문 항목 - 여행 성향 엔티티
 */

package com.heylocal.traveler.domain.travelon;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "TRAVEL_TYPE_GROUP")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TravelTypeGroup extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceTasteType placeTasteType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ActivityTasteType activityTasteType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SnsTasteType snsTasteType;

  public void registerAt(TravelOn travelOn) {
    this.travelOn = travelOn;
    if (travelOn.getTravelTypeGroup() != this) {
      travelOn.registerTravelTypeGroup(this);
    }
  }
}