/**
 * packageName    : com.heylocal.traveler.domain.user
 * fileName       : UserRole
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 서비스 사용자 종류 ENUM
 */

package com.heylocal.traveler.domain.user;

public enum UserRole {
  TRAVELER("여행자"), SERVICE_MANAGER("서비스 관리자"),
  ANONYMIZED("익명");

  private String value;

  UserRole(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
