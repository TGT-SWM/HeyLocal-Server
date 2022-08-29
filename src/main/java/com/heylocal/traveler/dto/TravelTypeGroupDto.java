package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.ActivityTasteType;
import com.heylocal.traveler.domain.travelon.PlaceTasteType;
import com.heylocal.traveler.domain.travelon.SnsTasteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TravelTypeGroupDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 유형 요청 DTO")
  public static class TravelTypeGroupRequest {
    private PlaceTasteType placeTasteType;
    private ActivityTasteType activityTasteType;
    private SnsTasteType snsTasteType;
  }
}
