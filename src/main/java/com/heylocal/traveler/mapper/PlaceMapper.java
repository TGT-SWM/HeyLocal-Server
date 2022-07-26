/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : PlaceMapper
 * author         : 우태균
 * date           : 2022/09/30
 * description    : Place 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.list.PlaceItemType;
import com.heylocal.traveler.dto.PlaceItemDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.PlaceDto.*;

@Mapper(uses = {RegionMapper.class}, builder = @Builder(disableBuilder = true), imports = {PlaceItemType.class})
public interface PlaceMapper {
  PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

  @Mapping(target = "id", source = "placeRequest.id")
  @Mapping(target = "opinionList", ignore = true)
  @Mapping(target = "placeItemList", ignore = true)
  @Mapping(target = "region", source = "region")
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  Place toEntity(PlaceRequest placeRequest, Region region);

  @Named("toPlaceResponseDto")
  PlaceResponse toPlaceResponseDto(Place place);

  @Mapping(target = "opinionSize", expression = "java(place.getOpinionList().size())")
  PlaceWithOpinionSizeResponse toPlaceWithOpinionSizeResponseDto(Place place);

  @Mapping(target = "region", ignore = true)
  @Mapping(target = "thumbnailUrl", ignore = true)
  @Mapping(target = "opinionList", ignore = true)
  @Mapping(target = "placeItemList", ignore = true)
  Place toEntity(PlaceItemDto.PlaceItemRequest placeItemRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @Mapping(target = "opinionList", ignore = true)
  @Mapping(target = "placeItemList", ignore = true)
  @Mapping(target = "category", source = "category")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "roadAddress", source = "roadAddress")
  @Mapping(target = "address", source = "address")
  @Mapping(target = "lat", source = "lat")
  @Mapping(target = "lng", source = "lng")
  @Mapping(target = "thumbnailUrl", source = "thumbnailUrl")
  void updatePlace(PlaceRequest placeRequest, @MappingTarget Place place);
}
