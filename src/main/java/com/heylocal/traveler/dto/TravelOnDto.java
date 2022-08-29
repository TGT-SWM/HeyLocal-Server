package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.heylocal.traveler.dto.RegionDto.RegionRequest;

public class TravelOnDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 생성을 위한 요청 DTO")
	public static class TravelOnRequest {
		private String title;
		private RegionRequest region;
		private LocalDate travelStartDate;
		private LocalDate travelEndDate;
		private String description;
		private TransportationType transportationType;
		private Set<MemberType> memberTypeSet = new HashSet<>();
		private Integer accommodationMaxCost;
		private Set<AccommodationType> accommodationTypeSet = new HashSet<>();
		private Integer foodMaxCost;
		private Set<FoodType> foodTypeSet = new HashSet<>();
		private Integer drinkMaxCost;
		private Set<DrinkType> drinkTypeSet = new HashSet<>();
		private TravelTypeGroupRequest travelTypeGroup;
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
