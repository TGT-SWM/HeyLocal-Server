package com.heylocal.traveler.domain.plan;

/**
 * 여행 상태
 */
public enum PlanStatus {
  COMING("다가오는 여행"), DONE("지난 여행"), DOING("현재 여행 중");

  private String value;

  PlanStatus(String value) {
    this.value = value;
  }
}
