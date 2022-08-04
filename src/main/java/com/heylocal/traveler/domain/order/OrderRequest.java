package com.heylocal.traveler.domain.order;

import com.heylocal.traveler.domain.user.User;

/**
 * 매칭 요청
 */
public class OrderRequest {
  private Long id;
  private User requester;
  private User receiver;
  private OrderSheet orderSheet;
  private OrderRequestStatus status;
}
