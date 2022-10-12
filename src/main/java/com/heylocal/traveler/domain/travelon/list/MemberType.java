/**
 * packageName    : com.heylocal.traveler.domain.travelon.list
 * fileName       : MemberType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 여행On 질문 항목 - 동행인 종류 ENUM
 */

package com.heylocal.traveler.domain.travelon.list;

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
