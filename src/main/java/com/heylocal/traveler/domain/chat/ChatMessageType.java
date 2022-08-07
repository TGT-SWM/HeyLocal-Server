package com.heylocal.traveler.domain.chat;

/**
 * 채팅 메시지 종류
 */
public enum ChatMessageType {
  PLAIN_TEXT("일반 텍스트"),
  IMAGE("사진"),
  LOCATION("지도 정보"),
  SCHEDULE_INFO("특정 스케줄 항목"),
  SCHEDULE_MODIFIED("스케줄 변경 알림");

  private String value;

  ChatMessageType(String value) {
    this.value = value;
  }
}
