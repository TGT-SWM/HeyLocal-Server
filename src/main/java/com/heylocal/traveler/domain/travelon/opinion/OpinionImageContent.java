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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ImageContentType imageContentType;

  public void setOpinion(Opinion opinion) {
    this.opinion = opinion;
    if (!opinion.getOpinionImageContentList().contains(this)) {
      opinion.setOpinionImageContentList(this);
    }
  }

  /**
   * 어떤 항목에 대한 이미지인지 구분하는 Enum
   * 서버·DB 내부에서만 사용될 예정이기 때문에, 내부 ENUM 으로 선언함.
   */
  public enum ImageContentType {
    GENERAL("전체"),
    RECOMMEND_FOOD("추천 음식 (음식점)"),
    RECOMMEND_DRINK_DESSERT("추천 음료 및 디저트 (카페)"),
    PHOTO_SPOT("사진 명소 (문화시설,관광지)");

    private String value;

    ImageContentType(String value) {
      this.value = value;
    }
  }
}
