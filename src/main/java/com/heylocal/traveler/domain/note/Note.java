package com.heylocal.traveler.domain.note;

import com.heylocal.traveler.domain.Score;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.user.User;

/**
 * 꿀팁 노트
 * TODO - https://swm13-tgt.atlassian.net/browse/S3T-308
 */
public class Note {
  private Long id;
  private User writer;
  private Place place;
  private Score score;
  private String opinion;

  //일반 평가 항목
  private EvaluationDegree kindness; //직원이 친절한가요
  private EvaluationDegree facilityCleanliness; //시설이 청결한가요
  private EvaluationDegree accessibility; //접근성이 좋나요
  private Boolean canParking; //주차장이 있나요
  private Boolean waiting; //웨이팅이 있나요
  private String photoSpotImageUrl; //사진 명소 - 예시 사진
  private String photoSpotText; //사진 명소 - 단답형
  private EvaluationDegree costPerformance; //가성비가 좋나요
  private String mood; //가게 분위기가 어떤가요 (단답형)

  //식당 및 주점 평가 항목
  private EvaluationDegree food; //음식맛이 어떤가요
  private EvaluationDegree toiletCleanliness; //화장실이 깨끗한가요
  private String recommendFood; //추천 음식


  //카페 평가 항목
  private EvaluationDegree drink; //음료 맛이 어떤가요
  private CoffeeType coffeeType; // 커피 스타일이 어떤가요
  private String recommendDrink; //추천 음료
  private String recommendDessert; //추천 디저트

  //놀거리 & 볼거리
  private String recommendToDo; //꼭 해봐야 하는 것
  private String recommendSnack; //추천 간식

  //숙소
  private EvaluationDegree streetNoise; //주변이 시끄럽나요
  private EvaluationDegree deafening; //방음이 잘 되나요
  private EvaluationDegree breakFast; //조식이 맛있나요
  private Boolean existsAmenity; //부대시설이 있나요
  private Boolean existsStore; //근처에 편의점이 있나요

  /**
   * 커피 종류
   */
  public enum CoffeeType {
    BITTER("쓴맛"), GENERAL("보통"), SOUR("신맛");

    private String value;

    CoffeeType(String value) {
      this.value = value;
    }
  }

  /**
   * 3단계 평가
   */
  public enum EvaluationDegree {
    GOOD("좋음"), BAD("나쁨"), NOT_BAD("보통");

    private String value;

    EvaluationDegree(String value) {
      this.value = value;
    }
  }
}
