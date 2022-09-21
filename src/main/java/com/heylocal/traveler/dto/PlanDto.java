package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.dto.PlaceItemDto.PlaceItemRequest;
import com.heylocal.traveler.dto.PlaceItemDto.PlaceItemResponse;
import com.heylocal.traveler.mapper.PlaceItemMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PlanDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "스케줄 생성을 위한 요청 DTO")
	public static class PlanCreateRequest {
		long travelOnId;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "스케줄 수정을 위한 요청 DTO")
	public static class PlanUpdateRequest {
		String title;
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
		long regionId;
		String regionState;
		String regionCity;
		// String regionThumbUrl;
		LocalDate startDate;
		LocalDate endDate;
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
	public static class PlanSchedulesRequest {
		List<ScheduleRequest> schedules;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 목록 수정을 위한 요청 DTO")
	public static class ScheduleRequest {
		List<PlaceItemRequest> places;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 정보 응답 DTO")
	public static class PlanPlacesResponse {
		LocalDate date;
		List<PlaceItemResponse> places;
	}
}
