package com.heylocal.traveler.domain.opinion;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 답변에 포함되는 이미지
 */
@Entity
@Table(name = "IMAGE_CONTENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class ImageContent extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Opinion opinion;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private Integer placedLineIndex;
}
