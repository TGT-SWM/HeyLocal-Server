package com.heylocal.traveler.domain.opinion;

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
