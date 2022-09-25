package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.HopeDrink;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.HopeDrinkDto.HopeDrinkResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface HopeDrinkMapper {
  HopeDrinkMapper INSTANCE = Mappers.getMapper(HopeDrinkMapper.class);

  @Mapping(target = "id", ignore = true)
  HopeDrink toEntity(TravelOn travelOn, DrinkType type);

  HopeDrinkResponse toResponseDto(HopeDrink hopeDrink);

  @AfterMapping
  default void registerTravelOnToEntity(TravelOn travelOn, @MappingTarget HopeDrink hopeDrink) {
    hopeDrink.registerAt(travelOn);
  }
}
