package com.heylocal.traveler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.*;
import com.heylocal.traveler.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.dto.OpinionImageContentDto.OpinionImageContentResponse;
import static com.heylocal.traveler.dto.PlaceDto.PlaceRequest;
import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import static com.heylocal.traveler.dto.UserDto.UserResponse;

public class OpinionDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ApiModel
	@Schema(description = "답변 생성을 위한 요청 DTO")
	public static class OpinionRequest {
		@ApiModelProperty("상세 설명")
		private String description;
		@Valid
		private PlaceRequest place;
		@ApiModelProperty("전체(일반) 사진 url\n순서대로 배치")
		private List<String> generalImgContentUrlList;
		@ApiModelProperty("추천 음식 (음식점) 사진 url\n순서대로 배치")
		private List<String> foodImgContentUrlList;
		@ApiModelProperty("추천 음료 및 디저트 (카페) 사진 url\n순서대로 배치")
		private List<String> drinkAndDessertImgContentUrlList;
		@ApiModelProperty("사진 명소 (관광지 및 문화시설) 사진 url\n순서대로 배치")
		private List<String> photoSpotImgContentUrlList;
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

		public Opinion toEntity(Place place, User author, TravelOn travelOn, Region region) {
			Opinion opinion = Opinion.builder()
					.description(description)
					.facilityCleanliness(facilityCleanliness)
					.costPerformance(costPerformance)
					.canParking(canParking)
					.waiting(waiting)
					.restaurantMoodType(restaurantMoodType)
					.recommendFoodDescription(recommendFoodDescription)
					.coffeeType(coffeeType)
					.cafeMoodType(cafeMoodType)
					.recommendToDo(recommendToDo)
					.recommendSnack(recommendSnack)
					.photoSpotDescription(photoSpotDescription)
					.streetNoise(streetNoise)
					.deafening(deafening)
					.hasBreakFast(hasBreakFast)
					.build();

			opinion.registerPlace(place);
			opinion.registerAuthor(author);
			opinion.registerTravelOn(travelOn);
			opinion.registerRegion(region);

			setEntityWithGeneralImgContents(opinion);
			setEntityWithFoodImgContents(opinion);
			setEntityWithDrinkAndDessertImgContents(opinion);
			setEntityWithPhotoSpotImgContents(opinion);

			return opinion;
		}

		@JsonIgnore
		private void setEntityWithGeneralImgContents(Opinion entity) {
			this.generalImgContentUrlList.stream().forEach(
					(item) -> {
						OpinionImageContent imgContentEntity = OpinionImageContent.builder()
								.url(item)
								.imageContentType(ImageContentType.GENERAL)
								.build();
						imgContentEntity.registerOpinion(entity);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithFoodImgContents(Opinion entity) {
			this.foodImgContentUrlList.stream().forEach(
					(item) -> {
						OpinionImageContent imgContentEntity = OpinionImageContent.builder()
								.url(item)
								.imageContentType(ImageContentType.RECOMMEND_FOOD)
								.build();
						imgContentEntity.registerOpinion(entity);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithDrinkAndDessertImgContents(Opinion entity) {
			this.drinkAndDessertImgContentUrlList.stream().forEach(
					(item) -> {
						OpinionImageContent imgContentEntity = OpinionImageContent.builder()
								.url(item)
								.imageContentType(ImageContentType.RECOMMEND_DRINK_DESSERT)
								.build();
						imgContentEntity.registerOpinion(entity);
					}
			);
		}

		@JsonIgnore
		private void setEntityWithPhotoSpotImgContents(Opinion entity) {
			this.photoSpotImgContentUrlList.stream().forEach(
					(item) -> {
						OpinionImageContent imgContentEntity = OpinionImageContent.builder()
								.url(item)
								.imageContentType(ImageContentType.PHOTO_SPOT)
								.build();
						imgContentEntity.registerOpinion(entity);
					}
			);
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "여행 On에 대한 답변 응답 DTO")
	public static class OpinionResponse {
		private long id;
		private String description;
		private UserResponse author;
		private PlaceResponse place;
		private List<String> generalImgContentUrlList;
		private List<String> foodImgContentUrlList;
		private List<String> drinkAndDessertImgContentUrlList;
		private List<String> photoSpotImgContentUrlList;

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

		public OpinionResponse(Opinion entity) {
			this.id = entity.getId();
			this.description = entity.getDescription();
			this.author = new UserResponse(entity.getAuthor());
			this.place = new PlaceResponse(entity.getPlace());

			//공통 질문
			this.facilityCleanliness = entity.getFacilityCleanliness();
			this.costPerformance = entity.getCostPerformance();
			this.canParking = entity.getCanParking();
			this.waiting = entity.getWaiting();

			//음식점 전용 항목
			this.restaurantMoodType = entity.getRestaurantMoodType();
			this.recommendFoodDescription = entity.getRecommendFoodDescription();

			//카페 전용 항목
			this.coffeeType = entity.getCoffeeType();
			this.recommendDrinkAndDessertDescription = entity.getRecommendDrinkAndDessertDescription();
			this.cafeMoodType = entity.getCafeMoodType();

			//문화시설, 관광명소 전용 항목
			this.recommendToDo = entity.getRecommendToDo();
			this.recommendSnack = entity.getRecommendSnack();
			this.photoSpotDescription = entity.getPhotoSpotDescription();

			//숙박 전용
			this.streetNoise = entity.getStreetNoise();
			this.deafening = entity.getDeafening();
			this.hasBreakFast = entity.getHasBreakFast();

			setAllImgContentsList(entity.getOpinionImageContentList());
		}

		@JsonIgnore
		private void setAllImgContentsList(List<OpinionImageContent> opinionImageContentEntityList) {
			this.generalImgContentUrlList = new ArrayList<>();
			this.foodImgContentUrlList = new ArrayList<>();
			this.drinkAndDessertImgContentUrlList = new ArrayList<>();
			this.photoSpotImgContentUrlList = new ArrayList<>();

			opinionImageContentEntityList.stream().forEach(
					(item) -> {
						ImageContentType imgType = item.getImageContentType();
						String imgUrl = item.getUrl();
						switch (imgType) {
							case GENERAL:
								generalImgContentUrlList.add(imgUrl);
								break;
							case RECOMMEND_FOOD:
								foodImgContentUrlList.add(imgUrl);
								break;
							case RECOMMEND_DRINK_DESSERT:
								drinkAndDessertImgContentUrlList.add(imgUrl);
								break;
							case PHOTO_SPOT:
								photoSpotImgContentUrlList.add(imgUrl);
								break;
						}
					}
			);
		}
	}
}
