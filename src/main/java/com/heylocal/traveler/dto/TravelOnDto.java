package com.heylocal.traveler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.heylocal.traveler.dto.PageDto.PageRequest;

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
		private long regionId;
		@NotNull
		private LocalDate travelStartDate;
		@NotNull
		private LocalDate travelEndDate;
		private String description;
		@NotNull
		private TransportationType transportationType;
		@NotEmpty
		private Set<MemberType> memberTypeSet = new HashSet<>();
		private int accommodationMaxCost;
		@NotEmpty
		private Set<AccommodationType> accommodationTypeSet = new HashSet<>();
		private int foodMaxCost;
		@NotEmpty
		private Set<FoodType> foodTypeSet = new HashSet<>();
		private int drinkMaxCost;
		@NotEmpty
		private Set<DrinkType> drinkTypeSet = new HashSet<>();
		@Valid
		private TravelTypeGroupRequest travelTypeGroup;

		/**
		 * 새로운 TravelOn 엔티티를 만드는 메서드
		 * @param author 이 존재하는 User 엔티티
		 * @param region 이미 존재하는 Region 엔티티
		 * @return
		 */
		public TravelOn toEntity(User author, Region region) {
			TravelOn travelOn;

			travelOn = TravelOn.builder()
					.title(title)
					.region(region)
					.author(author)
					.views(0)
					.travelStartDate(travelStartDate)
					.travelEndDate(travelEndDate)
					.description(description)
					.transportationType(transportationType)
					.accommodationMaxCost(accommodationMaxCost)
					.foodMaxCost(foodMaxCost)
					.drinkMaxCost(drinkMaxCost)
					.build();

			setEntityWithTravelMember(travelOn);
			setEntityWithHopeAccommodation(travelOn);
			setEntityWithHopeFood(travelOn);
			setEntityWithHopeDrink(travelOn);
			travelOn.registerTravelTypeGroup(travelTypeGroup.toEntity());

			return travelOn;
		}

		@JsonIgnore
		private void setEntityWithHopeDrink(TravelOn travelOn) {
			this.drinkTypeSet.stream().forEach(
					(item) -> {
						HopeDrink hopeDrink = HopeDrink.builder()
										.travelOn(travelOn)
										.type(item)
										.build();
						travelOn.addHopeDrink(hopeDrink);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithHopeFood(TravelOn travelOn) {
			this.foodTypeSet.stream().forEach(
					(item) -> {
						HopeFood hopeFood = HopeFood.builder()
								.travelOn(travelOn)
								.type(item)
								.build();
						travelOn.addHopeFood(hopeFood);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithHopeAccommodation(TravelOn travelOn) {
			this.accommodationTypeSet.stream().forEach(
					(item) -> {
						HopeAccommodation hopeAccommodation = HopeAccommodation.builder()
										.travelOn(travelOn)
										.type(item)
										.build();
						travelOn.addHopeAccommodation(hopeAccommodation);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithTravelMember(TravelOn travelOn) {
			this.memberTypeSet.stream().forEach(
					(item) -> {
						TravelMember travelMember = TravelMember.builder()
										.travelOn(travelOn)
										.memberType(item)
										.build();
						travelOn.addTravelMember(travelMember);
					}
			);
		}
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
		long id;
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
		private RegionDto.RegionResponse region;
		private LocalDateTime createdDateTime;
		private LocalDateTime modifiedDate;
		private UserDto.UserProfileResponse userProfile;
		private String description;
		private int views;
		private int opinionQuantity;

		public TravelOnSimpleResponse(TravelOn entity) {
			this.id = entity.getId();
			this.title = entity.getTitle();
			this.region = new RegionDto.RegionResponse(entity.getRegion());
			this.createdDateTime = entity.getCreatedDate();
			this.modifiedDate = entity.getModifiedDate();
			this.userProfile = new UserDto.UserProfileResponse(entity.getAuthor().getUserProfile(), 0); //Ranking 은 무조건 0으로 표시하도록 함
			this.description = entity.getDescription();
			this.views = entity.getViews();
			this.opinionQuantity = entity.getOpinionList().size();
		}
	}

	public enum TravelOnSortType {
		DATE, VIEWS, OPINIONS;
	}
}
