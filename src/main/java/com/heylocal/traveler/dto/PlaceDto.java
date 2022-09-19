package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.plan.list.PlaceItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.heylocal.traveler.dto.RegionDto.*;

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

		public Place toEntity(Region region) {
			Place place = Place.builder()
					.id(id)
					.category(category)
					.name(name)
					.roadAddress(roadAddress)
					.address(address)
					.lat(lat)
					.lng(lng)
					.region(region)
					.thumbnailUrl(thumbnailUrl)
					.link(kakaoLink)
					.build();

			return place;
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
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

		public PlaceItem toEntity() {
			Place place = Place.builder()
					.id(id)
					.category(category)
					.name(name)
					.roadAddress(roadAddress)
					.address(address)
					.lat(lat)
					.lng(lng)
					.link(link)
					.build();

			return PlaceItem.builder()
					.type(PlaceItemType.ORIGINAL)
					.place(place)
					.itemIndex(itemIndex)
					.originalPlaceId(null)
					.build();
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "플랜 내 장소 아이템 응답 DTO")
	public static class PlaceItemResponse {
		long id;
		String name;
		String address;
		String roadAddress;
		double lat;
		double lng;
	}
}
