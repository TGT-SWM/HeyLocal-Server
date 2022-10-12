/**
 * packageName    : com.heylocal.traveler.domain.travelon.list
 * fileName       : HopeDrink
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 여행On 질문 항목 - 희망 주류 엔티티
 */

package com.heylocal.traveler.domain.travelon.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "HOPE_DRINK")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class HopeDrink extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DrinkType type;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  public void registerAt(TravelOn travelOn) {
    this.travelOn = travelOn;
    if (!travelOn.getHopeDrinkSet().contains(this)) {
      travelOn.getHopeDrinkSet().add(this);
    }
  }
}
