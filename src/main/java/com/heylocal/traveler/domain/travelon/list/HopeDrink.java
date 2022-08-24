package com.heylocal.traveler.domain.travelon.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 희망 음주
 */
@Entity
@Table(name = "HOPE_DRINK")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class HopeDrink extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DrinkType type;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;
}
