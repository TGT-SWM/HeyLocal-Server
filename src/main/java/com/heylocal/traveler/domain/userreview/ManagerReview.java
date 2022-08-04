package com.heylocal.traveler.domain.userreview;

import com.heylocal.traveler.domain.Score;
import com.heylocal.traveler.domain.travel.Travel;
import com.heylocal.traveler.domain.user.User;

/**
 * 매니저 리뷰
 */
public class ManagerReview extends UserReview {
  private User manager;
  private Travel travel;
  private Score kindness;
  private Score responsiveness;
  private Score noteDetail;
  private Score notePrecision;
  private String otherOpinion;
}
