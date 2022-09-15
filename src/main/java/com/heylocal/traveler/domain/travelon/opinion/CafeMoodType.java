package com.heylocal.traveler.domain.travelon.opinion;

/**
 * 카페 분위기
 */
public enum CafeMoodType {
  MODERN("활기찬"), LARGE("격식있는"),
  CUTE("로맨틱한"), HIP("힙한");

  private String value;

  CafeMoodType(String value) {
    this.value = value;
  }
}
