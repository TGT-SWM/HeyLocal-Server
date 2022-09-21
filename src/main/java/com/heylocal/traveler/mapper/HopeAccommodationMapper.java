package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.HopeAccommodation;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.HopeAccommodationDto.HopeAccommodationResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface HopeAccommodationMapper {
  HopeAccommodationMapper INSTANCE = Mappers.getMapper(HopeAccommodationMapper.class);

  @Mapping(target = "id", ignore = true)
  HopeAccommodation toEntity(TravelOn travelOn, AccommodationType type);

  HopeAccommodationResponse toResponseDto(HopeAccommodation hopeAccommodation);

  @AfterMapping
  default void registerTravelOnToEntity(TravelOn travelOn, @MappingTarget HopeAccommodation hopeAccommodation) {
    hopeAccommodation.registerAt(travelOn);
  }
}