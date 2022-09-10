package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class OpinionImageContent {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On에 대한 답변 응답 DTO")
  public static class OpinionImageContentResponse {
    private long id;
    private String url;
    private int placedIndex;
  }
}
