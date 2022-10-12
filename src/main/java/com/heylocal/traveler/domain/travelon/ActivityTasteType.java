/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : ActivityTasteType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - 여행 활동 성향 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon;

public enum ActivityTasteType {
  HARD("부지런"), LAZY("느긋한");

  private String value;

  ActivityTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
