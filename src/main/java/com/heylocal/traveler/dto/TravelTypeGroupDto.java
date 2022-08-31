package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.ActivityTasteType;
import com.heylocal.traveler.domain.travelon.PlaceTasteType;
import com.heylocal.traveler.domain.travelon.SnsTasteType;
import com.heylocal.traveler.domain.travelon.TravelTypeGroup;
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

    public TravelTypeGroup toEntity() {
      return TravelTypeGroup.builder()
          .placeTasteType(placeTasteType)
          .activityTasteType(activityTasteType)
          .snsTasteType(snsTasteType)
          .build();
    }
  }
}
