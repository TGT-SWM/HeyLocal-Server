package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.mapper.context.S3UrlOpinionContext;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.OpinionDto.*;

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
  @Mapping(target = "description", source = "newOpinionRequest.description")
  @Mapping(target = "place", source = "place")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "region", source = "region")
  @Mapping(target = "opinionImageContentList", ignore = true)
  Opinion toEntity(NewOpinionRequestRequest newOpinionRequest, Place place, User author, TravelOn travelOn, Region region);

  @Mapping(target = "generalImgDownloadImgUrl", ignore = true)
  @Mapping(target = "foodImgDownloadImgUrl", ignore = true)
  @Mapping(target = "drinkAndDessertImgDownloadImgUrl", ignore = true)
  @Mapping(target = "photoSpotImgDownloadImgUrl", ignore = true)
  @Mapping(target = "place", qualifiedByName = "toPlaceResponseDto")
  @Mapping(target = "author", qualifiedByName = "toUserProfileResponseDtoByUserWithoutContext")
  @Mapping(target = "countAccept", ignore = true)
  OpinionWithPlaceResponse toWithPlaceResponseDto(Opinion opinion, @Context S3UrlOpinionContext s3UrlOpinionContext);

  @InheritConfiguration(name = "toWithPlaceResponseDto")
  @Mapping(target = "generalImgDownloadImgUrl", ignore = true)
  @Mapping(target = "foodImgDownloadImgUrl", ignore = true)
  @Mapping(target = "drinkAndDessertImgDownloadImgUrl", ignore = true)
  @Mapping(target = "photoSpotImgDownloadImgUrl", ignore = true)
  @Mapping(target = "author", qualifiedByName = "toUserProfileResponseDtoByUserWithoutContext")
  @Mapping(target = "countAccept", ignore = true)
  OpinionResponse toResponseDto(Opinion opinion, @Context S3UrlOpinionContext s3UrlOpinionContext);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "travelOn", ignore = true)
  @Mapping(target = "opinionImageContentList", ignore = true)
  @Mapping(target = "countAccept", ignore = true)
  @Mapping(target = "description", source = "opinionRequest.description")
  @Mapping(target = "place", source = "place")
  @Mapping(target = "region", source = "region")
  void updateOpinion(OpinionOnlyTextRequest opinionRequest, Region region, Place place, @MappingTarget Opinion opinion);

  @AfterMapping
  default void removeAllImgContentListOfEntity(@MappingTarget Opinion opinion) {
    opinion.removeAllOpinionImageContent();
  }

  @AfterMapping
  default void addCountAcceptToDto(@MappingTarget OpinionResponse response, Opinion opinion) {
    response.setCountAccept(opinion.getCountAccept());
  }

  @AfterMapping
  default void addCountAcceptToDto(@MappingTarget OpinionWithPlaceResponse response, Opinion opinion) {
    response.setCountAccept(opinion.getCountAccept());
  }
}
