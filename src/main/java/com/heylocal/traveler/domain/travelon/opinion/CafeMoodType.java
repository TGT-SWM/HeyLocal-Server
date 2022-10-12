/**
 * packageName    : com.heylocal.traveler.domain.travelon.opinion
 * fileName       : CafeMoodType
 * author         : 우태균
 * date           : 2022/09/15
 * description    : 여행On 답변 항목 - 카페 분위기 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.opinion;

/**
 * 카페 분위기
 */
public enum CafeMoodType {
  MODERN("활기찬"), LARGE("격식있는"),
  CUTE("로맨틱한"), HIP("힙한");

  private String value;

  CafeMoodType(String value) {
    this.value = value;
  }
}
