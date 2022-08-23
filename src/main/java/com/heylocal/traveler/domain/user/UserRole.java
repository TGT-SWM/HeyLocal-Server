package com.heylocal.traveler.domain.user;

/**
 * 사용자 종류
 */
public enum UserRole {
  TRAVELER("여행자"), SERVICE_MANAGER("서비스 관리자");

  private String value;

  UserRole(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
