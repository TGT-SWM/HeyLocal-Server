/**
 * packageName    : com.heylocal.traveler.domain.travelon.opinion
 * fileName       : EvaluationDegree
 * author         : 우태균
 * date           : 2022/08/25
 * description    : 여행On 답변 항목 - 총 5단계 평가 ENUM
 */

package com.heylocal.traveler.domain.travelon.opinion;

public enum EvaluationDegree {
  VERY_BAD("매우 나쁨"), BAD("나쁨"), NOT_BAD("보통"),
  GOOD("좋음"), VERY_GOOD("매우 좋음");

  private String value;

  EvaluationDegree(String value) {
    this.value = value;
  }
}
