/**
 * packageName    : com.heylocal.traveler.domain.place
 * fileName       : PlaceCategory
 * author         : 우태균
 * date           : 2022/10/06
 * description    : 장소 종류 ENUM,
 *                  카카오 장소 검색 API 기반의 장소 카테고리,
 *                  https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category-request-category-group-code
 */

package com.heylocal.traveler.domain.place;

public enum PlaceCategory {
  MT1("대형마트"),
  CS2("편의점"),
  PS3("어린이집, 유치원"),
  SC4("학교"),
  AC5("학원"),
  PK6("주차장"),
  OL7("주유소, 충전소"),
  SW8("지하철역"),
  BK9("은행"),
  CT1("문화시설"),
  AG2("중개업소"),
  PO3("공공기관"),
  AT4("관광명소"),
  AD5("숙박"),
  FD6("음식점"),
  CE7("카페"),
  HP8("병원"),
  PM9("약국"),
  ETC("기타");

  private String value;

  PlaceCategory(String value) {
    this.value = value;
  }
}
