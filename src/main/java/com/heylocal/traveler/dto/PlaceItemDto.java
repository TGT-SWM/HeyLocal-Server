package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.place.PlaceCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class PlaceItemDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "플랜 내 장소 아이템 수정을 위한 요청 DTO")
  public static class PlaceItemRequest {
    long id;
    int itemIndex;
    PlaceCategory category;
    String name;
    String roadAddress;
    String address;
    double lat;
    double lng;
    String link;
    LocalDateTime arrivalTime;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "플랜 내 장소 아이템 응답 DTO")
  public static class PlaceItemResponse {
    long id;
    PlaceCategory category;
    String name;
    String address;
    String roadAddress;
    double lat;
    double lng;
    String link;
    LocalDateTime arrivalTime;
  }
}
