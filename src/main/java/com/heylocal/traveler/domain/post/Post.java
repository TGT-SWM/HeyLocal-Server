package com.heylocal.traveler.domain.post;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.theme.Theme;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.visitreview.VisitReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * 포스트
 */

@Entity
@Table(name = "POST")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post extends BaseTimeEntity {
  @Id @GeneratedValue
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
}
