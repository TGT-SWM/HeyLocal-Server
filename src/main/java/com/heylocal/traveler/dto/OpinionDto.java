package com.heylocal.traveler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.CoffeeType;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.OpinionImageContentDto.*;
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
		@ApiModelProperty(value = "직원이 친절한가요", required = true)
		@NotNull
		private EvaluationDegree kindness;
		@ApiModelProperty(value = "시설이 청결한가요", required = true)
		@NotNull
		private EvaluationDegree facilityCleanliness;
		@ApiModelProperty(value = "접근성이 좋나요", required = true)
		@NotNull
		private EvaluationDegree accessibility;
		@ApiModelProperty(value = "가성비가 좋나요", required = true)
		@NotNull
		private EvaluationDegree costPerformance;
		@ApiModelProperty("주차장이 있나요")
		private boolean canParking;
		@ApiModelProperty("웨이팅이 있나요")
		private boolean waiting;
		@ApiModelProperty("사진 명소 - 예시 사진 S3 URL")
		private String photoSpotImageUrl;
		@ApiModelProperty("사진 명소 설명 - 단답형")
		private String photoSpotText;
		@ApiModelProperty("분위기가 어떤가요 (단답형)")
		private String mood;
		@ApiModelProperty("화장실이 깨끗한가요")
		private EvaluationDegree toiletCleanliness;

		//음식점 전용 항목
		@ApiModelProperty("음식맛이 어떤가요")
		private EvaluationDegree food;
		@ApiModelProperty("추천 음식 설명")
		private String recommendFood;

		//카페 전용 항목
		@ApiModelProperty("음료 맛이 어떤가요")
		private EvaluationDegree drink;
		@ApiModelProperty("커피 스타일이 어떤가요")
		private CoffeeType coffeeType;
		@ApiModelProperty("추천 음료")
		private String recommendDrink;
		@ApiModelProperty("추천 디저트")
		private String recommendDessert;

		//문화시설, 관광명소 전용 항목
		@ApiModelProperty("꼭 해봐야 하는 것")
		private String recommendToDo;
		@ApiModelProperty("추천 간식 설명")
		private String recommendSnack;

		//숙박 전용
		@ApiModelProperty("주변이 시끄럽나요")
		private EvaluationDegree streetNoise;
		@ApiModelProperty("방음이 잘 되나요")
		private EvaluationDegree deafening;
		@ApiModelProperty("조식이 맛있나요")
		private EvaluationDegree breakFast;
		@ApiModelProperty("부대시설이 있나요")
		private boolean existsAmenity;
		@ApiModelProperty("근처에 편의점이 있나요")
		private boolean existsStore;

		public Opinion toEntity(Place place, User author, TravelOn travelOn, Region region) {
			Opinion opinion = Opinion.builder()
					.description(description)
					.kindness(kindness)
					.facilityCleanliness(facilityCleanliness)
					.accessibility(accessibility)
					.costPerformance(costPerformance)
					.canParking(canParking)
					.waiting(waiting)
					.photoSpotImageUrl(photoSpotImageUrl)
					.photoSpotText(photoSpotText)
					.mood(mood)
					.toiletCleanliness(toiletCleanliness)
					.food(food)
					.recommendFood(recommendFood)
					.drink(drink)
					.coffeeType(coffeeType)
					.recommendDrink(recommendDrink)
					.recommendDessert(recommendDessert)
					.recommendToDo(recommendToDo)
					.recommendSnack(recommendSnack)
					.streetNoise(streetNoise)
					.deafening(deafening)
					.breakFast(breakFast)
					.existsAmenity(existsAmenity)
					.existsStore(existsStore)
					.build();

			opinion.registerPlace(place);
			opinion.registerAuthor(author);
			opinion.registerTravelOn(travelOn);
			opinion.registerRegion(region);

			return opinion;
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
		private List<OpinionImageContentResponse> opinionImageContentList;

		//공통 질문
		private EvaluationDegree kindness;
		private EvaluationDegree facilityCleanliness;
		private EvaluationDegree accessibility;
		private EvaluationDegree costPerformance;
		private Boolean canParking;
		private Boolean waiting;
		private String photoSpotImageUrl;
		private String photoSpotText;
		private String mood;
		private EvaluationDegree toiletCleanliness;

		//음식점 전용 항목
		private EvaluationDegree food;
		private String recommendFood;

		//카페 전용 항목
		private EvaluationDegree drink;
		private CoffeeType coffeeType;
		private String recommendDrink;
		private String recommendDessert;

		//문화시설, 관광명소 전용 항목
		private String recommendToDo;
		private String recommendSnack;

		//숙박 전용
		private EvaluationDegree streetNoise;
		private EvaluationDegree deafening;
		private EvaluationDegree breakFast;
		private Boolean existsAmenity;
		private Boolean existsStore;

		public OpinionResponse(Opinion entity) {
			this.id = entity.getId();
			this.description = entity.getDescription();
			this.author = new UserResponse(entity.getAuthor());
			this.place = new PlaceResponse(entity.getPlace());

			//공통 질문
			this.kindness = entity.getKindness();
			this.facilityCleanliness = entity.getFacilityCleanliness();
			this.accessibility = entity.getAccessibility();
			this.costPerformance = entity.getCostPerformance();
			this.canParking = entity.getCanParking();
			this.waiting = entity.getWaiting();
			this.photoSpotImageUrl = entity.getPhotoSpotImageUrl();
			this.photoSpotText = entity.getPhotoSpotText();
			this.mood = entity.getMood();
			this.toiletCleanliness = entity.getToiletCleanliness();

			//음식점 전용 항목
			this.food = entity.getFood();
			this.recommendFood = entity.getRecommendFood();

			//카페 전용 항목
			this.drink = entity.getDrink();
			this.coffeeType = entity.getCoffeeType();
			this.recommendDrink = entity.getRecommendDrink();
			this.recommendDessert = entity.getRecommendDessert();

			//문화시설, 관광명소 전용 항목
			this.recommendToDo = entity.getRecommendToDo();
			this.recommendSnack = entity.getRecommendSnack();

			//숙박 전용
			this.streetNoise = entity.getStreetNoise();
			this.deafening = entity.getDeafening();
			this.breakFast = entity.getBreakFast();
			this.existsAmenity = entity.getExistsAmenity();
			this.existsStore = entity.getExistsStore();

			setOpinionImageContentList(entity.getOpinionImageContentList());
		}

		@JsonIgnore
		public void setOpinionImageContentList(List<OpinionImageContent> opinionImageContentEntityList) {
			this.opinionImageContentList = opinionImageContentEntityList.stream().map(OpinionImageContentResponse::new).collect(Collectors.toList());
		}
	}
}
