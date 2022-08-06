package com.heylocal.traveler.domain.order;

/**
 * 주의사항 종류
 */
public enum PrecautionType {
  ALLERGY("알러지가 있어요"), LIMITED_MOBILITY("거동이 불편해요");

  private String value;

  PrecautionType(String value) {
    this.value = value;
  }
}
