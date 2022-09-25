package com.heylocal.traveler.domain.article;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 관리자가 작성하는 여행 관련 아티클
 */
@Entity
@Table(name = "ARTICLE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Article extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Lob
  private String body;
}
