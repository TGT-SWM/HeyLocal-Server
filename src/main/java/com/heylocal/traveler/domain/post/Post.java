package com.heylocal.traveler.domain.post;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.theme.Theme;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.visitreview.VisitReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 포스트
 */

@Entity
@Table(name = "POST")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Post extends BaseTimeEntity {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Theme theme;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Place place;

  private String title;

  @Lob
  private String textBody;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Manager writer;

  private Long views;

  //양방향 설정

  @OneToMany(mappedBy = "post") //포스트 삭제 시, S3에 저장된 이미지 파일를 따로 작업하여 제거해야하므로 cascade 설정 X
  private List<ImageContent> imageContentList = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<VisitReview> visitReviewList = new ArrayList<>();
}
