package com.heylocal.traveler.domain.travelon.list;

/**
 * 누구와 함께 가는지 종류
 */
public enum MemberType {
  ALONE("혼자"), CHILD("아이와"), PARENT("부모님과"),
  COUPLE("연인과"), FRIEND("친구와"), PET("반려동물과");

  private String value;

  MemberType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
