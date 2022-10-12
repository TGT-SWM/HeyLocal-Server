/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : HopeFoodDto
 * author         : 우태균
 * date           : 2022/09/05
 * description    : 여행On 질문 항목 중, 희망 음식 관련 DTO
 */

package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.FoodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class HopeFoodDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On의 희망 음식 응답 DTO")
  public static class HopeFoodResponse {
    private long id;
    private FoodType type;
  }
}
