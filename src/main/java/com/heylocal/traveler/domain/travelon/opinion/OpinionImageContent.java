/**
 * packageName    : com.heylocal.traveler.domain.travelon.opinion
 * fileName       : OpinionImageContent
 * author         : 우태균
 * date           : 2022/09/04
 * description    : 여행On 답변에 포함되는 이미지 엔티티
 */

package com.heylocal.traveler.domain.travelon.opinion;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "OPINION_IMAGE_CONTENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OpinionImageContent extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Opinion opinion;

  @Column(unique = true)
  private String objectKeyName; //S3에 저장된 Object의 Key(name), null인 경우 S3에 업로드가 안됐거나 AWS SNS가 동작하지 않은 것임

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ImageContentType imageContentType;

  public void setOpinion(Opinion opinion) {
    this.opinion = opinion;

    if (!opinion.getOpinionImageContentList().contains(this)) {
      opinion.addOpinionImageContent(this);
    }
  }

  public void releaseOpinion() {
    if (this.opinion != null) {
      this.opinion.removeOpinionImageContent(this);
    }
    this.opinion = null;
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
