package com.heylocal.traveler.domain.visitreview;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.post.Post;
import com.heylocal.traveler.domain.user.Traveler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 방문리뷰
 */

@Entity
@Table(name = "VISIT_REVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class VisitReview extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Post post;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Traveler writer;

  private String imageUrl;

  @Column(nullable = false)
  private String body;
}
