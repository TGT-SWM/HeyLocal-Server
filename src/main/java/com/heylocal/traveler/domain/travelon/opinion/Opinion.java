package com.heylocal.traveler.domain.travelon.opinion;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
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
 * 여행On 의 답변(의견)
 */
@Entity
@Table(name = "OPINION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Opinion extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(length = 785)
  private String description;

  @ManyToOne(optional = false)
  private User author;

  @ManyToOne(optional = false)
  private Region region;

  @ManyToOne(optional = false)
  private Place place;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TravelOn travelOn;

  // [일반]
//  @Enumerated(EnumType.STRING)
//  @Column(nullable = false)
//  private EvaluationDegree kindness; //직원이 친절한가요

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree facilityCleanliness; //시설이 청결한가요

//  @Enumerated(EnumType.STRING)
//  @Column(nullable = false)
//  private EvaluationDegree accessibility; //접근성이 좋나요

  @Column(nullable = false)
  private Boolean canParking; //주차장이 있나요

  @Column(nullable = false)
  private Boolean waiting; //웨이팅이 있나요

//  private String photoSpotImageUrl; //사진 명소 - 예시 사진

//  @Column(length = 512)
//  private String photoSpotText; //사진 명소 - 단답형

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree costPerformance; //가성비가 좋나요

//  private String mood; //가게 분위기가 어떤가요 (단답형)

//  @Enumerated(EnumType.STRING)
//  private EvaluationDegree toiletCleanliness; //화장실이 깨끗한가요

  // [음식점]
//  @Enumerated(EnumType.STRING)
//  private EvaluationDegree food; //음식맛이 어떤가요

  @Enumerated(EnumType.STRING)
  private RestaurantMoodType restaurantMoodType; //식당 분위기가 어떤가요

  private String recommendFoodDescription; //추천 음식 설명

  // [카페]
//  @Enumerated(EnumType.STRING)
//  private EvaluationDegree drink; //음료 맛이 어떤가요

  @Enumerated(EnumType.STRING)
  private CoffeeType coffeeType; // 커피 스타일이 어떤가요

  private String recommendDrinkAndDessertDescription; //추천 음료·디저트 설명

//  private String recommendDessert; //추천 디저트
  @Enumerated(EnumType.STRING)
  private CafeMoodType cafeMoodType; //카페 분위기는 어떤가요

  // [문화시설, 관광명소]
  private String recommendToDo; //꼭 해봐야 하는 것

  private String recommendSnack; //추천 간식

  private String photoSpotDescription; //사진 명소 설명

  // [숙박]
  @Enumerated(EnumType.STRING)
  private EvaluationDegree streetNoise; //주변이 시끄럽나요

  @Enumerated(EnumType.STRING)
  private EvaluationDegree deafening; //방음이 잘 되나요

//  @Enumerated(EnumType.STRING)
//  private EvaluationDegree breakFast; //조식이 맛있나요
  private Boolean hasBreakFast; //조식이 나오나요

//  private Boolean existsAmenity; //부대시설이 있나요

//  private Boolean existsStore; //근처에 편의점이 있나요

  // 양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "opinion", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) //Opinion 이 삭제되어도, S3에 저장된 경로를 알아야하므로 Cascade를 Persist만 설정
  private List<OpinionImageContent> opinionImageContentList = new ArrayList<>();

  public void registerTravelOn(TravelOn travelOn) {
    this.travelOn = travelOn;
    if (!travelOn.getOpinionList().contains(this)) {
      travelOn.addOpinion(this);
    }
  }

  public void registerPlace(Place place) {
    this.place = place;
    if (!place.getOpinionList().contains(this)) {
      place.addOpinion(this);
    }
  }

  public void registerAuthor(User author) {
    this.author = author;
    if (!author.getOpinionList().contains(this)) {
      author.addOpinion(this);
    }
  }

  public void registerRegion(Region region) {
    this.region = region;
    if (!region.getOpinionList().contains(this)) {
      region.addOpinion(this);
    }
  }

  public void addOpinionImgContent(OpinionImageContent imgContent) {
    this.opinionImageContentList.add(imgContent);
    if (imgContent.getOpinion() != this) {
      imgContent.registerOpinion(this);
    }
  }

  // 이하는 업데이트 메서드

  public void updatePlace(Place newValue) {
    if (!Objects.isNull(this.place)) {
      this.place.removeOpinion(this);
    }
    registerPlace(newValue);
  }

  public void updateRegion(Region newValue) {
    if (!Objects.isNull(this.region)) {
      this.region.removeOpinion(this);
    }
    registerRegion(newValue);
  }

  public void updateDescription(String newValue) {
    this.description = newValue;
  }

  public void updateFacilityCleanliness(EvaluationDegree newValue) {
    this.facilityCleanliness = newValue;
  }

  public void updateCanParking(Boolean newValue) {
    this.canParking = newValue;
  }

  public void updateWaiting(Boolean newValue) {
    this.waiting = newValue;
  }

  public void updateCostPerformance(EvaluationDegree newValue) {
    this.costPerformance = newValue;
  }

  public void updateRestaurantMoodType(RestaurantMoodType newValue) {
    this.restaurantMoodType = newValue;
  }

  public void updateRecommendFoodDescription(String newValue) {
    this.recommendFoodDescription = newValue;
  }

  public void updateCoffeeType(CoffeeType newValue) {
    this.coffeeType = newValue;
  }

  public void updateRecommendDrinkAndDessertDescription(String newValue) {
    this.recommendDrinkAndDessertDescription = newValue;
  }

  public void updateCafeMoodType(CafeMoodType newValue) {
    this.cafeMoodType = newValue;
  }

  public void updateRecommendToDo(String newValue) {
    this.recommendToDo = newValue;
  }

  public void updateRecommendSnack(String newValue) {
    this.recommendSnack = newValue;
  }

  public void updatePhotoSpotDescription(String newValue) {
    this.photoSpotDescription = newValue;
  }

  public void updateStreetNoise(EvaluationDegree newValue) {
    this.streetNoise = newValue;
  }

  public void updateDeafening(EvaluationDegree newValue) {
    this.deafening = newValue;
  }

  public void updateHasBreakFast(Boolean newValue) {
    this.hasBreakFast = newValue;
  }
}