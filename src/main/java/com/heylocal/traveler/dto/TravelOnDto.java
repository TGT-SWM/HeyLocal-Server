package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.heylocal.traveler.dto.HopeAccommodationDto.HopeAccommodationResponse;
import static com.heylocal.traveler.dto.HopeDrinkDto.HopeDrinkResponse;
import static com.heylocal.traveler.dto.HopeFoodDto.HopeFoodResponse;
import static com.heylocal.traveler.dto.PageDto.PageRequest;
import static com.heylocal.traveler.dto.RegionDto.RegionResponse;
import static com.heylocal.traveler.dto.TravelMemberDto.TravelMemberResponse;
import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupResponse;
import static com.heylocal.traveler.dto.UserDto.UserResponse;

public class TravelOnDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 생성을 위한 요청 DTO")
	public static class TravelOnRequest {
		@NotEmpty
		@Length(max = 255)
		private String title;
		@NotNull
		@Positive
		private Long regionId;
		@NotNull
		private LocalDate travelStartDate;
		@NotNull
		private LocalDate travelEndDate;
		private String description;
		@NotNull
		private TransportationType transportationType;
		@NotEmpty
		@Builder.Default
		private Set<MemberType> memberTypeSet = new HashSet<>();
		private int accommodationMaxCost;
		@NotEmpty
		@Builder.Default
		private Set<AccommodationType> accommodationTypeSet = new HashSet<>();
		@NotEmpty
		@Builder.Default
		private Set<FoodType> foodTypeSet = new HashSet<>();
		@NotEmpty
		@Builder.Default
		private Set<DrinkType> drinkTypeSet = new HashSet<>();
		@Valid
		private TravelTypeGroupRequest travelTypeGroup;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 목록 조회 요청 DTO")
	public static class AllTravelOnGetRequest {
		@ApiParam(value = "지역(Region) id(pk)", required = false)
		private Long regionId;
		@ApiParam(value = "답변 있는 것(true), 없는 것(false), 전체(null)", required = false)
		private Boolean withOpinions;
		@ApiParam(value = "정렬 기준(DATE, VIEWS, OPINIONS)", required = true)
		private TravelOnSortType sortBy;
		@ApiParam(value = "페이징", required = true)
		private PageRequest pageRequest;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 응답 DTO")
	public static class TravelOnResponse {
		private long id;
		private String title;
		private String description;
		private int views;
		private RegionResponse region;
		private UserResponse author;
		private LocalDate travelStartDate;
		private LocalDate travelEndDate;
		private TransportationType transportationType;
		private Set<TravelMemberResponse> travelMemberSet;
		private Integer accommodationMaxCost;
		private Set<HopeAccommodationResponse> hopeAccommodationSet;
		private Set<HopeFoodResponse> hopeFoodSet;
		private Set<HopeDrinkResponse> hopeDrinkSet;
		private TravelTypeGroupResponse travelTypeGroup;
		private LocalDateTime createdDateTime;
		private LocalDateTime modifiedDate;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On 목록에 띄우기 위한 간략한 여행 On 응답 DTO")
	public static class TravelOnSimpleResponse {
		private long id;
		private String title;
		private RegionResponse region;
		private LocalDateTime createdDateTime;
		private LocalDateTime modifiedDate;
		private UserResponse author;
		private String description;
		private int views;
		private int opinionQuantity;
	}

	public enum TravelOnSortType {
		DATE, VIEWS, OPINIONS;
	}
}
