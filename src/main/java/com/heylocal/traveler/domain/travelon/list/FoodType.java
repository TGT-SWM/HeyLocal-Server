/**
 * packageName    : com.heylocal.traveler.domain.travelon.list
 * fileName       : FoodType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - 음식 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.list;

/**
 * 선호 음식 종류
 */
public enum FoodType {
  KOREAN("한식"), CHINESE("중식"), JAPANESE("일식"),
  WESTERN("양식"), GLOBAL("세계음식");

  private String value;

  FoodType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
