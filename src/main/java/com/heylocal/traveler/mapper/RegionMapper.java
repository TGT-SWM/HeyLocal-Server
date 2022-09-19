package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface RegionMapper {
  RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

  RegionResponse toResponseDto(Region region);
}
