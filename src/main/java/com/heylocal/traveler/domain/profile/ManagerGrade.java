package com.heylocal.traveler.domain.profile;

/**
 * 매니저 등급
 */
public enum ManagerGrade {
  INTERN("인턴"), JUNIOR("주니어"), SENIOR("시니어");

  private String value;

  ManagerGrade(String value) {
    this.value = value;
  }
}
