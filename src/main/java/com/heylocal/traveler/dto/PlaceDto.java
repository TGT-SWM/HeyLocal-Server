package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.place.PlaceCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

public class PlaceDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ApiModel
	@Schema(description = "장소 상세 정보 요청 DTO")
	public static class PlaceRequest {
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 장소 id", required = true)
		@Positive
		private long id;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 장소 카테고리", required = true)
		@NotNull
		private PlaceCategory category;
		@ApiModelProperty(value = "장소 이름", required = true)
		@NotEmpty
		private String name;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 장소 도로명 주소", required = true)
		@NotEmpty
		private String roadAddress;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 장소 구주소", required = true)
		@NotEmpty
		private String address;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 위도값 (x)", required = true)
		@Positive
		private double lat;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 경도값 (y)", required = true)
		@Positive
		private double lng;
		private String thumbnailUrl;
		@ApiModelProperty(value = "카카오 장소 검색 API 에서 응답받은 카카오맵 link", required = true)
		@NotEmpty
		private String kakaoLink;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@Schema(description = "장소 상세 정보 응답 DTO")
	public static class PlaceResponse {
		private long id;
		private PlaceCategory category;
		private String name;
		private String roadAddress;
		private String address;
		private double lat;
		private double lng;
		private RegionResponse region;
		private String thumbnailUrl;
		private String kakaoLink;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@Schema(description = "장소 상세 정보 응답 DTO")
	public static class PlaceWithOpinionSizeResponse extends PlaceResponse {
		private int opinionSize;
	}

}
