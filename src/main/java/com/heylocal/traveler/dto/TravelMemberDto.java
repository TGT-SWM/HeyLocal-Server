package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TravelMemberDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On의 여행인원 정보 응답 DTO")
  public static class TravelMemberResponse {
    private long id;
    private MemberType type;
  }
}
