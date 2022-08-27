package com.heylocal.traveler.domain.travelon;

/**
 * 자동차 여부 종류
 */
public enum TransportationType {
  OWN_CAR("자차"), RENT_CAR("렌트카"), PUBLIC("대중교통");

  private String value;

  TransportationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
