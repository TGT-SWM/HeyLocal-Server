package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class RegionDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "지역 선택 요청 DTO")
	public static class RegionRequest {
		private String state;
		private String city;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "지역 정보 응답 DTO")
	public static class RegionResponse {
		long id;
	}
}
