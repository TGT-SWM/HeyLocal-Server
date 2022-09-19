package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.ActivityTasteType;
import com.heylocal.traveler.domain.travelon.PlaceTasteType;
import com.heylocal.traveler.domain.travelon.SnsTasteType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

public class TravelTypeGroupDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 유형 요청 DTO")
  public static class TravelTypeGroupRequest {
    @NotNull
    private PlaceTasteType placeTasteType;
    @NotNull
    private ActivityTasteType activityTasteType;
    @NotNull
    private SnsTasteType snsTasteType;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 유형 응답 DTO")
  public static class TravelTypeGroupResponse {
    private long id;
    private PlaceTasteType placeTasteType;
    private ActivityTasteType activityTasteType;
    private SnsTasteType snsTasteType;
  }
}
