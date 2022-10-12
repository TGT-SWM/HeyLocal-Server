/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : TransportationType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - 교통수단 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon;

public enum TransportationType {
  OWN_CAR("자차"), RENT_CAR("렌트카"), PUBLIC("대중교통");

  private String value;

  TransportationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
