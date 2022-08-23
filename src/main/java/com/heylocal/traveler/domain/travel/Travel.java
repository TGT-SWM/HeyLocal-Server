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

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private TravelOn travelOn;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User user;

  //양방향 설정

}
