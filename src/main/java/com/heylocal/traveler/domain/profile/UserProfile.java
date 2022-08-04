package com.heylocal.traveler.domain.profile;

import com.heylocal.traveler.domain.user.User;

/**
 * 사용자(여행자, 매니저) 프로필 부모 클래스
 */
public class UserProfile {
  private Long id;
  private User user; //유니크키
  private Integer point;
}
