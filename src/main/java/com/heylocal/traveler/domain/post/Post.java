package com.heylocal.traveler.domain.post;

import com.heylocal.traveler.domain.theme.Theme;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.visitreview.VisitReview;
import com.heylocal.traveler.domain.user.User;

import java.util.List;

/**
 * 포스트
 */
public class Post {
  private Long id;
  private Theme theme;
  private Place place; // TODO https://swm13-tgt.atlassian.net/browse/S3T-306
  private String title;
  private String textBody;
  private List<PostImage> postImageList; //유니크 키
  private Long views;
  private User writer;
  private List<VisitReview> visitReviewList;
}
