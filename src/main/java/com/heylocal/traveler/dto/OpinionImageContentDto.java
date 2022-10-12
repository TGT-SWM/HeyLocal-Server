/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : OpinionImageContentDto
 * author         : 우태균
 * date           : 2022/09/26
 * description    : 여행On 답변에 포함되는 이미지 관련 DTO
 */

package com.heylocal.traveler.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;

public class OpinionImageContentDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "답변 이미지 생성 요청")
  public static class OpinionImageContentRequest {
    private String objectKeyName;
    private ImageContentType imageContentType;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "답변 이미지 응답 DTO")
  public static class OpinionImageContentResponse {
    private long id;
    private ImageContentType imageContentType;
    private String objectKeyName;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "답변 이미지 개수 DTO")
  public static class ImageContentQuantity {
    @ApiModelProperty("등록할 전체(일반) 사진 개수 (3개 이하)")
    @Max(3)
    private int generalImgQuantity;
    @ApiModelProperty("등록할 추천 음식 사진 개수 (3개 이하)")
    @Max(3)
    private int foodImgQuantity;
    @ApiModelProperty("등록할 추천 음료 및 디저트 (카페) 사진 개수 (3개 이하)")
    @Max(3)
    private int drinkAndDessertImgQuantity;
    @ApiModelProperty("등록할 사진 명소 (관광지 및 문화시설) 사진 개수 (3개 이하)")
    @Max(3)
    private int photoSpotImgQuantity;
  }
}
