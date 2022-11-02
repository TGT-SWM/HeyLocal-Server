package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MenuDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "장소(음식점·카페)의 메뉴 정보 응답")
  public static class MenuResponse {
    private String name;
    private String price;
    private String photo;
  }
}
