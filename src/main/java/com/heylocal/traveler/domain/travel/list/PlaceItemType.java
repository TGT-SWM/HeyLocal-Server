package com.heylocal.traveler.domain.travel.list;

/**
 * 스케줄표의 장소 항목 타입
 */
public enum PlaceItemType {
  ORIGINAL("원 장소 항목"), SUB("대체 장소 항목");

  private String value;

  PlaceItemType(String value) {
    this.value = value;
  }
}
