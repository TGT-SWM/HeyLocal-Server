package com.heylocal.traveler.domain.travelon.list;

/**
 * 선호 술 종류
 */
public enum DrinkType {
  SOJU("소주"), BEAR("맥주"), WINE("와인"),
  MAKGEOLLI("막걸리"), LIQUOR("양주"), NO_ALCOHOL("안마심");

  private String value;

  DrinkType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
