package com.heylocal.traveler.domain.note;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.StarScore;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.user.Manager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 꿀팁 노트
 */

@Entity
@Table(name = "NOTE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Note extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Manager writer;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Place place;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StarScore score;

  @Column(length = 512)
  private String opinion;

  // [일반]
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree kindness; //직원이 친절한가요

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree facilityCleanliness; //시설이 청결한가요

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree accessibility; //접근성이 좋나요

  @Column(nullable = false)
  private Boolean canParking; //주차장이 있나요

  @Column(nullable = false)
  private Boolean waiting; //웨이팅이 있나요

  private String photoSpotImageUrl; //사진 명소 - 예시 사진

  @Column(length = 512)
  private String photoSpotText; //사진 명소 - 단답형

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree costPerformance; //가성비가 좋나요

  private String mood; //가게 분위기가 어떤가요 (단답형)

  @Enumerated(EnumType.STRING)
  private EvaluationDegree toiletCleanliness; //화장실이 깨끗한가요

  // [음식점]
  @Enumerated(EnumType.STRING)
  private EvaluationDegree food; //음식맛이 어떤가요

  private String recommendFood; //추천 음식


  // [카페]
  @Enumerated(EnumType.STRING)
  private EvaluationDegree drink; //음료 맛이 어떤가요

  @Enumerated(EnumType.STRING)
  private CoffeeType coffeeType; // 커피 스타일이 어떤가요

  private String recommendDrink; //추천 음료

  private String recommendDessert; //추천 디저트

  // [문화시설, 관광명소]
  private String recommendToDo; //꼭 해봐야 하는 것

  private String recommendSnack; //추천 간식

  // [숙박]
  @Enumerated(EnumType.STRING)
  private EvaluationDegree streetNoise; //주변이 시끄럽나요

  @Enumerated(EnumType.STRING)
  private EvaluationDegree deafening; //방음이 잘 되나요

  @Enumerated(EnumType.STRING)
  private EvaluationDegree breakFast; //조식이 맛있나요

  private Boolean existsAmenity; //부대시설이 있나요

  private Boolean existsStore; //근처에 편의점이 있나요

}
