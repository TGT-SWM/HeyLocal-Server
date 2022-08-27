package com.heylocal.traveler.domain.travelon;

/**
 * 장소 취향
 */
public enum PlaceTasteType {
  FAMOUS("인기있는 곳"), FRESH("참신한 곳");

  private String value;

  PlaceTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
