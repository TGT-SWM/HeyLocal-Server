package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class PlanDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "스케줄 생성을 위한 요청 DTO")
	public static class PlanRequest {
		long id;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "스케줄 정보 응답 DTO")
	public static class PlanResponse {
		// 장소 정보를 제외한 스케줄 기본 정보
		// 지역, 여행 기간 등
		long id;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "스케줄 정보를 지금 여행 중, 다가오는 여행, 지난 여행으로 분류한 응답 DTO")
	public static class PlanListResponse {
		List<PlanResponse> past;
		List<PlanResponse> ongoing;
		List<PlanResponse> upcoming;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 목록 수정을 위한 요청 DTO")
	public static class PlanPlacesRequest {
		long id;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 정보 응답 DTO")
	public static class PlanPlacesResponse {
		long id;
	}
}
