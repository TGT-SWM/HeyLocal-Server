/**
 * packageName    : com.heylocal.traveler.exception.code
 * fileName       : ErrorCode
 * author         : 우태균
 * date           : 2022/08/20
 * description    : 모든 에러 코드 ENUM 이 구현해야 하는 인터페이스
 */

package com.heylocal.traveler.exception.code;

/**
 * <pre>
 * 모든 에러 코드 enum이 구현해야하는 인터페이스
 * <pre/>
 */
public interface ErrorCode {
  String getDescription();
}
