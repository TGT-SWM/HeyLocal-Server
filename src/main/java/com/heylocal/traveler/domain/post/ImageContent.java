package com.heylocal.traveler.domain.post;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 포스트에 포함되는 이미지
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

  @ManyToOne
  @JoinColumn(nullable = false)
  private Post post;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer placedLineIndex;
}
