package com.heylocal.traveler.domain.chat;

/**
 * 채팅 메시지 종류
 */
public enum ChatMessageType {
  PLAIN_TEXT("일반 텍스트"),
  IMAGE("사진");

  private String value;

  ChatMessageType(String value) {
    this.value = value;
  }
}
