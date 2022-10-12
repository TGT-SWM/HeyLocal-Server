/**
 * packageName    : com.heylocal.traveler.domain.article
 * fileName       : ArticleImageContent
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 게시글에 포함된 이미지 정보 엔티티
 */

package com.heylocal.traveler.domain.article;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 아티클에 포함되는 이미지
 */
@Entity
@Table(name = "ARTICLE_IMAGE_CONTENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ArticleImageContent extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Article article;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private Integer placedLineIndex;
}
