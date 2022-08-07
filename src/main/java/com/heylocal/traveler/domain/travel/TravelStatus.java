package com.heylocal.traveler.domain.travel;

/**
 * 여행 상태
 */
public enum TravelStatus {
  COMING("다가오는 여행"), DONE("지난 여행"), DOING("현재 여행 중");

  private String value;

  TravelStatus(String value) {
    this.value = value;
  }
}
