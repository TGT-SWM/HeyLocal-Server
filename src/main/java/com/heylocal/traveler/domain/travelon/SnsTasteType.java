package com.heylocal.traveler.domain.travelon;

/**
 * SNS 여부
 */
public enum SnsTasteType {
  YES("SNS 유명 장소"), NO("SNS 안함");

  private String value;

  SnsTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
