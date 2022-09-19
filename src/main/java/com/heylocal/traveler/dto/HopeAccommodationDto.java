package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class HopeAccommodationDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On의 희망 숙소 응답 DTO")
  public static class HopeAccommodationResponse {
    private long id;
    private AccommodationType type;
  }
}
