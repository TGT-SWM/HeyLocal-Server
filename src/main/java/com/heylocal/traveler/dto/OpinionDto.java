package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.opinion.CafeMoodType;
import com.heylocal.traveler.domain.travelon.opinion.CoffeeType;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.RestaurantMoodType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.dto.OpinionImageContentDto.ImageContentQuantity;
import static com.heylocal.traveler.dto.PlaceDto.PlaceRequest;
import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;

public class OpinionDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@ApiModel
	@Schema(description = "답변 생성을 위한 요청 DTO")
	public static class NewOpinionRequestRequest extends OpinionOnlyTextRequest {
		@Valid
		private ImageContentQuantity quantity;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@ApiModel
	@Schema(description = "답변 항목 중, 텍스트 정보만 포함하는 DTO")
	public static class OpinionOnlyTextRequest {
		@ApiModelProperty("상세 설명")
		private String description;
		@Valid
		private PlaceRequest place;
		@ApiModelProperty(value = "시설이 청결한가요", required = true)
		@NotNull
		private EvaluationDegree facilityCleanliness;
		@ApiModelProperty(value = "가성비가 좋나요", required = true)
		@NotNull
		private EvaluationDegree costPerformance;
		@ApiModelProperty("주차장이 있나요")
		private boolean canParking;
		@ApiModelProperty("웨이팅이 있나요")
		private boolean waiting;

		//음식점 전용 항목
		@ApiModelProperty("식당 분위기가 어떤가요")
		private RestaurantMoodType restaurantMoodType;

		@ApiModelProperty("추천 음식 설명")
		private String recommendFoodDescription;

		//카페 전용 항목
		@ApiModelProperty("커피 스타일이 어떤가요")
		private CoffeeType coffeeType;

		@ApiModelProperty("추천 음료·디저트 설명")
		private String recommendDrinkAndDessertDescription;

		@ApiModelProperty("카페 분위기는 어떤가요")
		private CafeMoodType cafeMoodType;

		//문화시설, 관광명소 전용 항목
		@ApiModelProperty("꼭 해봐야 하는 것")
		private String recommendToDo;
		@ApiModelProperty("추천 간식 설명")
		private String recommendSnack;

		@ApiModelProperty("사진 명소 설명")
		private String photoSpotDescription;

		//숙박 전용
		@ApiModelProperty("주변이 시끄럽나요")
		private EvaluationDegree streetNoise;
		@ApiModelProperty("방음이 잘 되나요")
		private EvaluationDegree deafening;

		@ApiModelProperty("조식이 나오나요")
		private Boolean hasBreakFast;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@Schema(description = "여행 On에 대한 답변 응답 DTO")
	public static class OpinionResponse {
		private long id;
		private String description;
		private UserProfileResponse author;
		@Builder.Default
		private List<String> generalImgDownloadImgUrl = new ArrayList<>();
		@Builder.Default
		private List<String> foodImgDownloadImgUrl = new ArrayList<>();
		@Builder.Default
		private List<String> drinkAndDessertImgDownloadImgUrl = new ArrayList<>();
		@Builder.Default
		private List<String> photoSpotImgDownloadImgUrl = new ArrayList<>();
		private int countAccept;
		private LocalDateTime createdDate;
		private LocalDateTime modifiedDate;

		//공통 질문
		private EvaluationDegree facilityCleanliness;
		private EvaluationDegree costPerformance;
		private Boolean canParking;
		private Boolean waiting;

		//음식점 전용 항목
		private RestaurantMoodType restaurantMoodType;
		private String recommendFoodDescription;

		//카페 전용 항목
		private CoffeeType coffeeType;
		private String recommendDrinkAndDessertDescription;
		private CafeMoodType cafeMoodType;

		//문화시설, 관광명소 전용 항목
		private String recommendToDo;
		private String recommendSnack;
		private String photoSpotDescription;

		//숙박 전용
		private EvaluationDegree streetNoise;
		private EvaluationDegree deafening;
		private Boolean hasBreakFast;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	@Schema(description = "여행 On에 대한 답변 및 관련 장소 응답 DTO")
	public static class OpinionWithPlaceResponse extends OpinionResponse {
		private PlaceResponse place;
	}
}
