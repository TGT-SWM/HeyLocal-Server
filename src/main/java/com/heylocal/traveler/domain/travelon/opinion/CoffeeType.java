/**
 * packageName    : com.heylocal.traveler.domain.travelon.opinion
 * fileName       : CoffeeType
 * author         : 우태균
 * date           : 2022/08/25
 * description    : 여행On 답변 항목 - 커피 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.opinion;

/**
 * 커피 종류
 */
public enum CoffeeType {
  BITTER("쓴맛"), GENERAL("보통"), SOUR("신맛");

  private String value;

  CoffeeType(String value) {
    this.value = value;
  }
}
