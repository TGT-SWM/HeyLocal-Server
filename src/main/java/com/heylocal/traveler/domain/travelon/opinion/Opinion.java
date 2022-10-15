package com.heylocal.traveler.domain.travelon.opinion;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행On 의 답변(의견)
 */
@Entity
@Table(name = "OPINION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree facilityCleanliness; //시설이 청결한가요

  @Column(nullable = false)
  private Boolean canParking; //주차장이 있나요

  @Column(nullable = false)
  private Boolean waiting; //웨이팅이 있나요

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EvaluationDegree costPerformance; //가성비가 좋나요

  //[음식점]
  @Enumerated(EnumType.STRING)
  private RestaurantMoodType restaurantMoodType; //식당 분위기가 어떤가요

  private String recommendFoodDescription; //추천 음식 설명

  // [카페]
  @Enumerated(EnumType.STRING)
  private CoffeeType coffeeType; // 커피 스타일이 어떤가요

  private String recommendDrinkAndDessertDescription; //추천 음료·디저트 설명

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

  private Boolean hasBreakFast; //조식이 나오나요

  // 양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "opinion", fetch = FetchType.LAZY)
  private List<OpinionImageContent> opinionImageContentList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "opinion", fetch = FetchType.LAZY)
  private List<PlaceItem> placeItemList = new ArrayList<>();

  public void setTravelOn(TravelOn travelOn) {
    if (this.travelOn != null) {
      this.travelOn.removeOpinion(this);
    }

    this.travelOn = travelOn;
    if (!travelOn.getOpinionList().contains(this)) {
      travelOn.addOpinion(this);
    }
  }

  public void setPlace(Place place) {
    if (this.place != null) {
      this.place.removeOpinion(this);
    }

    this.place = place;
    if (!place.getOpinionList().contains(this)) {
      place.addOpinion(this);
    }
  }

  public void setAuthor(User author) {
    if (this.author != null) {
      this.author.removeOpinion(this);
    }

    this.author = author;
    if (!author.getOpinionList().contains(this)) {
      author.addOpinion(this);
    }
  }

  public void setRegion(Region region) {
    if (this.region != null) {
      this.region.removeOpinion(this);
    }

    this.region = region;
    if (!region.getOpinionList().contains(this)) {
      region.addOpinion(this);
    }
  }

  public void addOpinionImageContent(OpinionImageContent imgContent) {
    this.opinionImageContentList.add(imgContent);
    if (imgContent.getOpinion() != this) {
      imgContent.setOpinion(this);
    }
  }

  public void removeOpinionImageContent(OpinionImageContent target) {
    this.opinionImageContentList.remove(target);
  }

  public void removeAllOpinionImageContent() {
    this.opinionImageContentList.clear();
  }

  public void registerPlaceItem(PlaceItem placeItem) {
    this.placeItemList.add(placeItem);
    if (placeItem.getOpinion() != this) {
      placeItem.registerOpinion(this);
    }
  }

  public int getCountAccept() {
    return (int) placeItemList.stream()
            .distinct()
            .count();
  }
}