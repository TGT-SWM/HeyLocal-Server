package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.plan.list.PlaceItemType;
import com.heylocal.traveler.dto.PlaceItemDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true), imports = {PlaceItemType.class, PlaceMapper.class})
public interface PlaceItemMapper {

  PlaceItemMapper INSTANCE = Mappers.getMapper(PlaceItemMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "schedule", ignore = true)
  @Mapping(target = "type", expression = "java(PlaceItemType.ORIGINAL)")
  @Mapping(target = "place", expression = "java(PlaceMapper.INSTANCE.toEntity(placeItemRequest))")
  @Mapping(target = "originalPlaceId", ignore = true)
  PlaceItem toPlaceItemEntity(PlaceItemDto.PlaceItemRequest placeItemRequest);

  @Mapping(target = "name", source = "placeItem.place.name")
  @Mapping(target = "address", source = "placeItem.place.address")
  @Mapping(target = "roadAddress", source = "placeItem.place.roadAddress")
  @Mapping(target = "lat", source = "placeItem.place.lat")
  @Mapping(target = "lng", source = "placeItem.place.lng")
  @Mapping(target = "category", source = "placeItem.place.category")
  @Mapping(target = "link", source = "placeItem.place.link")
  PlaceItemDto.PlaceItemResponse toPlaceItemResponseDto(PlaceItem placeItem);
}
