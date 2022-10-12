/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : SnsTasteType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - SNS 관련 취향 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon;

public enum SnsTasteType {
  YES("SNS 유명 장소"), NO("SNS 안함");

  private String value;

  SnsTasteType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
