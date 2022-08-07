package com.heylocal.traveler.domain;

/**
 * 별점 단위
 */
public enum StarScore {
  ZERO_HALF(0.5f),
  ONE(1.0f), ONE_HALF(1.5f),
  TWO(2.0f), TWO_HALF(2.5f),
  THREE(3.0f), THREE_HALF(3.5f),
  FOUR(4.0f), FOUR_HALF(4.5f),
  FIVE(5.0f);

  private float value;

  StarScore(float value) {
    this.value = value;
  }
}
