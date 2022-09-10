package com.heylocal.traveler.domain.travelon.opinion;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 답변에 포함되는 이미지
 */
@Entity
@Table(name = "OPINION_IMAGE_CONTENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class OpinionImageContent extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Opinion opinion;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private Integer placedIndex;
}
