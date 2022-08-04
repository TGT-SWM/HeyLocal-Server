package com.heylocal.traveler.domain.travel;

import com.heylocal.traveler.domain.chat.ChatRoom;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.user.User;

import java.util.List;

/**
 * 여행
 */
public class Travel {
  private Long id;
  private TravelStatus status;
  private OrderSheet orderSheet;
  private User manager;
  private List<Schedule> scheduleList;
  private Boolean isFixed;
  private ChatRoom chatRoom;
}
