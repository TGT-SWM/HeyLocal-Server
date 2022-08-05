package com.heylocal.traveler.domain.payment;

/**
 * 거래 종류
 */
public enum PaymentType {
  MATCH("여행자 -> 매니저");

  private String value;

  PaymentType(String value) {
    this.value = value;
  }
}
