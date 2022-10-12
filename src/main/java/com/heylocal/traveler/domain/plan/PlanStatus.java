/**
 * packageName    : com.heylocal.traveler.domain.plan
 * fileName       : PlanStatus
 * author         : 우태균
 * date           : 2022/08/26
 * description    : 여행 계획표 상태 구분 ENUM
 */

package com.heylocal.traveler.domain.plan;

public enum PlanStatus {
  COMING("다가오는 여행"), DONE("지난 여행"), DOING("현재 여행 중");

  private String value;

  PlanStatus(String value) {
    this.value = value;
  }
}
