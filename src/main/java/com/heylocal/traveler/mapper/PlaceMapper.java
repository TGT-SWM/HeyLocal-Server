package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.PlaceDto.*;

@Mapper(uses = {RegionMapper.class}, builder = @Builder(disableBuilder = true))
public interface PlaceMapper {
  PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "opinionList", ignore = true)
  @Mapping(target = "placeItemList", ignore = true)
  @Mapping(target = "link", source = "placeRequest.kakaoLink")
  @Mapping(target = "region", source = "region")
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  Place toEntity(PlaceRequest placeRequest, Region region);

  @Mapping(target = "kakaoLink", source = "link")
  PlaceResponse toPlaceResponseDto(Place place);

  @Mapping(target = "name", source = "placeItem.place.name")
  @Mapping(target = "address", source = "placeItem.place.address")
  @Mapping(target = "roadAddress", source = "placeItem.place.roadAddress")
  @Mapping(target = "lat", source = "placeItem.place.lat")
  @Mapping(target = "lng", source = "placeItem.place.lng")
  PlaceItemResponse toPlaceItemResponseDto(PlaceItem placeItem);
}
