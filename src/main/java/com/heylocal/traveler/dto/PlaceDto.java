package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PlaceDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 상세 정보 요청 DTO")
	public static class PlaceRequest {
		@Positive
		private long id;
		@NotNull
		private PlaceCategory category;
		@NotEmpty
		private String name;
		@NotEmpty
		private String roadAddress;
		@NotEmpty
		private String address;
		@Positive
		private double lat;
		@Positive
		private double lng;
		@NotEmpty
		private String kakaoLink;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 상세 정보 응답 DTO")
	public static class PlaceResponse {
		long id;
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

		public PlaceItemResponse(PlaceItem placeItem) {
			Place place = placeItem.getPlace();
			this.id = place.getId();
			this.name = place.getName();
			this.address = place.getAddress();
			this.roadAddress = place.getRoadAddress();
			this.lat = place.getLat();
			this.lng = place.getLng();
		}
	}
}
