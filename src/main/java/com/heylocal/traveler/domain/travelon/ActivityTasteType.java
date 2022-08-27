package com.heylocal.traveler.domain.travelon;

/**
 * 여행 스타일
 */
public enum ActivityTasteType {
  HARD("부지런"), LAZY("느긋한");

  private String value;

  ActivityTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
