package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.HopeFood;
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
