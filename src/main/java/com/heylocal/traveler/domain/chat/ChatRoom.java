package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.travel.Travel;
import com.heylocal.traveler.domain.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방
 */
public class ChatRoom {
  private Long id;
  private User traveler;
  private User manager;
  private List<ChatMessage> chatMessageList = new ArrayList<>();
}
