package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.HopeFood;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.HopeFoodDto.HopeFoodResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface HopeFoodMapper {
  HopeFoodMapper INSTANCE = Mappers.getMapper(HopeFoodMapper.class);

  @Mapping(target = "id", ignore = true)
  HopeFood toEntity(TravelOn travelOn, FoodType type);

  HopeFoodResponse toResponseDto(HopeFood hopeFood);

  @AfterMapping
  default void registerTravelOnToEntity(TravelOn travelOn, @MappingTarget HopeFood hopeFood) {
    hopeFood.registerAt(travelOn);
  }
}