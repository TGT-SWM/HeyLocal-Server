/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : RegionDto
 * author         : 우태균
 * date           : 2022/09/01
 * description    : 시·도, 시 지역 관련 DTO
 */

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

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "특정 주소가 특정 지역의 주소인지 확인하여 응답하는 DTO")
	public static class RegionAddressCheckResponse {
		private boolean isSameRegionAddress;
	}
}
