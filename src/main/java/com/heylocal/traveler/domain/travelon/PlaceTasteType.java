/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : PlaceTasteType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - 장소 취향 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon;

public enum PlaceTasteType {
  FAMOUS("인기있는 곳"), FRESH("참신한 곳");

  private String value;

  PlaceTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
