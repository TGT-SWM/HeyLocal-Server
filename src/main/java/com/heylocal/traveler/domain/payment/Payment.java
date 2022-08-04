package com.heylocal.traveler.domain.payment;

import com.heylocal.traveler.domain.travel.Travel;

/**
 * 현지머니 거래 내역
 * Travel에 있는 User 정보를 가지고 사용/획득을 구분하면 됨
 */
public class Payment {
  private long id;
  private Travel travel; //유니크키
  private Integer point; //사용·획득한 현지머니
}
