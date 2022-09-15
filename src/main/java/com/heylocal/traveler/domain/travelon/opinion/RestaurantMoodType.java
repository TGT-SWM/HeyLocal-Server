package com.heylocal.traveler.domain.travelon.opinion;

/**
 * 음식점 분위기
 */
public enum RestaurantMoodType {
  LIVELY("활기찬"), FORMAL("격식있는"), ROMANTIC("로맨틱한"),
  HIP("힙한"), COMFORTABLE("편안한");

  private String value;

  RestaurantMoodType(String value) {
    this.value = value;
  }
}
