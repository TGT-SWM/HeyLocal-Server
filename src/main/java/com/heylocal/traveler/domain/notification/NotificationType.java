package com.heylocal.traveler.domain.notification;

/**
 * 알림 타입
 */
public enum NotificationType {
  MATCH_REQUEST("매칭 요청"), MATCH_ACCEPTED("매칭 수락"),
  MATCH_DONE("매칭 완료"), SERVICE("서비스 공지");

  private String value;

  NotificationType(String value) {
    this.value = value;
  }
}
