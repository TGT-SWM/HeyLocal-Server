/**
 * packageName    : com.heylocal.traveler.domain.plan.list
 * fileName       : PlaceItemType
 * author         : 우태균
 * date           : 2022/08/29
 * description    : 여행 일정표의 방문 장소 구분 ENUM
 */

package com.heylocal.traveler.domain.plan.list;

public enum PlaceItemType {
  ORIGINAL("원 장소 항목"), SUB("대체 장소 항목");

  private String value;

  PlaceItemType(String value) {
    this.value = value;
  }
}
