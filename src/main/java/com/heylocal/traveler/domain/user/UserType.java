package com.heylocal.traveler.domain.user;

/**
 * 사용자 타입
 */
public enum UserType {
  TRAVELER("여행자"), MANAGER("매니저"), SERVICE_MANAGER("서비스 관리자");

  private String value;

  UserType(String value) {
    this.value = value;
  }
}
