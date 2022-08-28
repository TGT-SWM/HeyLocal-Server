package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TravelOnDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 생성을 위한 요청 DTO")
	public static class TravelOnRequest {
		long id;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 응답 DTO")
	public static class TravelOnResponse {
		long id;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 목록에 띄우기 위한 간략한 여행 On 응답 DTO")
	public static class TravelOnSimpleResponse {
		long id;
	}

	public enum TravelOnSortType {
		DATE, VIEWS, OPINIONS
	}
}
