/**
 * packageName    : com.heylocal.traveler.domain.travelon.list
 * fileName       : DrinkType
 * author         : 우태균
 * date           : 2022/08/29
 * description    : 여행On 질문 항목 - 술 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.list;

public enum DrinkType {
  SOJU("소주"), BEER("맥주"), WINE("와인"),
  MAKGEOLLI("막걸리"), LIQUOR("양주"), NO_ALCOHOL("안마심");

  private String value;

  DrinkType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
