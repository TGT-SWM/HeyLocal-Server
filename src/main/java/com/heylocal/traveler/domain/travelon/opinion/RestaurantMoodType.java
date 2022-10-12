/**
 * packageName    : com.heylocal.traveler.domain.travelon.opinion
 * fileName       : RestaurantMoodType
 * author         : 우태균
 * date           : 2022/09/15
 * description    : 여행On 답변 항목 - 음식점 분위 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.opinion;

public enum RestaurantMoodType {
  LIVELY("활기찬"), FORMAL("격식있는"), ROMANTIC("로맨틱한"),
  HIP("힙한"), COMFORTABLE("편안한");

  private String value;

  RestaurantMoodType(String value) {
    this.value = value;
  }
}
