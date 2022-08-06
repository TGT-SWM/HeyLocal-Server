package com.heylocal.traveler.domain.order;

/**
 * 매칭 요청 상태
 */
public enum OrderRequestStatus {
  RECEIVED("매칭 요청을 받은 직후 상태"), MANAGER_ACCEPT("매니저가 수락한 상태 (결제 대기 중)"),
  PAYED("결제를 완료한 상태");

  private String value;

  OrderRequestStatus(String value) {
    this.value = value;
  }
}
