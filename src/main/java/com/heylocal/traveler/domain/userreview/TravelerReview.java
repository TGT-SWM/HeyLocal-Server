package com.heylocal.traveler.domain.userreview;

import com.heylocal.traveler.domain.travel.Travel;

/**
 * 여행자 의견 남기기
 */
public class TravelerReview extends UserReview {
  private Travel travel;
  private String message;
}
