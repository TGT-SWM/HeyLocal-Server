/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : TokenCode
 * author         : 우태균
 * date           : 2022/08/20
 * description    : 인증·인가 토큰 관련 오류 코드 ENUM
 */

package com.heylocal.traveler.exception.code;

public enum TokenCode implements ErrorCode {
  NOT_EXIST_TOKEN_USER_ID("Token 클레임의 사용자 pk 값이 존재하지 않는 값입니다.");

  private String description;

  TokenCode(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
