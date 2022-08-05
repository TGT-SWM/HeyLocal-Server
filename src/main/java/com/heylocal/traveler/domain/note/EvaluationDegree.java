package com.heylocal.traveler.domain.note;

/**
 * 5단계 평가
 */
public enum EvaluationDegree {
  VERY_BAD("매우 나쁨"), BAD("나쁨"), NOT_BAD("보통"),
  GOOD("좋음"), VERY_GOOD("매우 좋음");

  private String value;

  EvaluationDegree(String value) {
    this.value = value;
  }
}
