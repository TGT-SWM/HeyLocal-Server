package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.HopeDrink;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class HopeDrinkDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On의 희망 주류 응답 DTO")
  public static class HopeDrinkResponse {
    private long id;
    private DrinkType type;
  }
}
