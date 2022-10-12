/**
 * packageName    : com.heylocal.traveler.domain.notification
 * fileName       : NotificationType
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 알림 종류 ENUM
 */

package com.heylocal.traveler.domain.notification;

/**
 * 알림 타입
 */
public enum NotificationType {
  NEW_OPINION("새 답변 등록"), ACCEPTED_ANSWER("답변 채택"),
  TRAVEL("여행 관련 알림"), SERVICE("서비스 공지");

  private String value;

  NotificationType(String value) {
    this.value = value;
  }
}
