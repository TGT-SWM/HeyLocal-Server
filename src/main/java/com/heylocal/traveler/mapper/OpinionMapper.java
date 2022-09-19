package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.dto.OpinionDto.OpinionRequest;
import static com.heylocal.traveler.dto.OpinionDto.OpinionResponse;

/**
 * target에 Lombok 의 @Builder 를 사용하면 @AfterMapping 이 제대로 동작하지 않는 버그가 존재함.
 * 따라서, builder = @Builder(disableBuilder = true) 를 설정함.
 */
@Mapper(uses = {UserMapper.class, PlaceMapper.class}, imports = {OpinionImageContent.class}, builder = @Builder(disableBuilder = true))
public interface OpinionMapper {
  OpinionMapper INSTANCE = Mappers.getMapper(OpinionMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "description", source = "opinionRequest.description")
  @Mapping(target = "place", source = "place")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "region", source = "region")
  @Mapping(target = "opinionImageContentList", ignore = true)
  Opinion toEntity(OpinionRequest opinionRequest, Place place, User author, TravelOn travelOn, Region region);

  @Mapping(target = "generalImgContentUrlList", ignore = true)
  @Mapping(target = "foodImgContentUrlList", ignore = true)
  @Mapping(target = "drinkAndDessertImgContentUrlList", ignore = true)
  @Mapping(target = "photoSpotImgContentUrlList", ignore = true)
  OpinionResponse toResponseDto(Opinion opinion);

  @AfterMapping
  default void updateResponseDtoImgContentList(Opinion opinion, @MappingTarget OpinionResponse opinionResponse) {
    List<String> generalUrlList = new ArrayList<>();
    List<String> foodUrlList = new ArrayList<>();
    List<String> drinkAndDessertUrlList = new ArrayList<>();
    List<String> photoSpotUrlList = new ArrayList<>();

    opinion.getOpinionImageContentList().stream().forEach(
        (item) -> {
          ImageContentType type = item.getImageContentType();
          switch (type) {
            case GENERAL:
              generalUrlList.add(item.getUrl());
              break;
            case RECOMMEND_FOOD:
              foodUrlList.add(item.getUrl());
              break;
            case RECOMMEND_DRINK_DESSERT:
              drinkAndDessertUrlList.add(item.getUrl());
              break;
            case PHOTO_SPOT:
              photoSpotUrlList.add(item.getUrl());
              break;
          }
        }
    );

    opinionResponse.setGeneralImgContentUrlList(generalUrlList);
    opinionResponse.setFoodImgContentUrlList(foodUrlList);
    opinionResponse.setDrinkAndDessertImgContentUrlList(drinkAndDessertUrlList);
    opinionResponse.setPhotoSpotImgContentUrlList(photoSpotUrlList);
  }

  @AfterMapping
  default void updateEntityImgContentList(OpinionRequest opinionRequest, @MappingTarget Opinion opinion) {
    opinionRequest.getGeneralImgContentUrlList().stream().forEach(
        (item) -> {
          OpinionImageContent imgContentEntity = OpinionImageContent.builder()
              .url(item)
              .imageContentType(ImageContentType.GENERAL)
              .build();
          imgContentEntity.setOpinion(opinion);
        }
    );
    opinionRequest.getFoodImgContentUrlList().stream().forEach(
        (item) -> {
          OpinionImageContent imgContentEntity = OpinionImageContent.builder()
              .url(item)
              .imageContentType(ImageContentType.RECOMMEND_FOOD)
              .build();
          imgContentEntity.setOpinion(opinion);
        }
    );
    opinionRequest.getDrinkAndDessertImgContentUrlList().stream().forEach(
        (item) -> {
          OpinionImageContent imgContentEntity = OpinionImageContent.builder()
              .url(item)
              .imageContentType(ImageContentType.RECOMMEND_DRINK_DESSERT)
              .build();
          imgContentEntity.setOpinion(opinion);
        }
    );
    opinionRequest.getPhotoSpotImgContentUrlList().stream().forEach(
        (item) -> {
          OpinionImageContent imgContentEntity = OpinionImageContent.builder()
              .url(item)
              .imageContentType(ImageContentType.PHOTO_SPOT)
              .build();
          imgContentEntity.setOpinion(opinion);
        }
    );
  }

}
