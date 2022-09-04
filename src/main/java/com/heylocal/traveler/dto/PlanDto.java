package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.dto.PlaceDto.PlaceItemResponse;
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
		long regionId;
		String regionState;
		String regionCity;
		// String regionThumbUrl;
		LocalDate startDate;
		LocalDate endDate;

		public PlanResponse(Plan plan) {
			TravelOn travelOn = plan.getTravelOn();
			Region region = travelOn.getRegion();

			this.id = plan.getId();
			this.regionId = region.getId();
			this.regionState = region.getState();
			this.regionCity = region.getCity();
			this.startDate = travelOn.getTravelStartDate();
			this.endDate = travelOn.getTravelEndDate();
		}
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
		List<PlaceItemResponse> places;

		public PlanPlacesResponse(DaySchedule daySchedule) {
			List<PlaceItem> placeItems = daySchedule.getPlaceItemList();
			this.places = placeItems.stream()
					.map(PlaceItemResponse::new)
					.collect(Collectors.toList());
		}
	}
}
