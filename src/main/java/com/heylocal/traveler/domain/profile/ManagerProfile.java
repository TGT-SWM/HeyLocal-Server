package com.heylocal.traveler.domain.profile;

import com.heylocal.traveler.domain.*;
import com.heylocal.traveler.domain.userreview.ManagerReview;

import java.util.List;

/**
 * 매니저 프로필
 */
public class ManagerProfile extends UserProfile {
  private ManagerGrade grade;
  private String imageUrl;
  private Integer totalMatchNum;
  private ManagerResponseTime responseTime;
  private List<Region> regionList;
  private Boolean matchReceive;
  private Float kindnessAvg;
  private Float responsivenessAvg;
  private Float noteDetailAvg;
  private Float notePrecisionAvg;
  private List<ManagerReview> managerReviewList;
  private String introduction;
}
