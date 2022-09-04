package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class PlaceDto {
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
	}
}
