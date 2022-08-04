package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.user.User;

/**
 * 채팅 메시지
 */
public class ChatMessage {
  private Long id;
  private ChatMessageType type;
  private User sender;
  private String message;
  private Boolean read;
}
