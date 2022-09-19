package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

public class RegionDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "지역 선택 요청 DTO")
	public static class RegionRequest {
		@NotEmpty
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
		private long id;
		private String state;
		private String city;
	}
}
