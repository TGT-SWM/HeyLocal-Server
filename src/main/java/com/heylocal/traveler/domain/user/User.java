package com.heylocal.traveler.domain.user;

/**
 * 사용자(여행자, 매니저, 서비스관리자) 부모 클래스
 * TODO https://swm13-tgt.atlassian.net/browse/S3T-298 반영해야함
 */
public class User {
  private long id;
  private String accountId;
  private String password;
  private String nickname;
  private String phoneNumber;
  private UserType type;
}
