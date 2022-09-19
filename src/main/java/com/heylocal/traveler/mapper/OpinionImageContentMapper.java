package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.OpinionImageContentDto.OpinionImageContentResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface OpinionImageContentMapper {
  OpinionImageContentMapper INSTANCE = Mappers.getMapper(OpinionImageContentMapper.class);

  OpinionImageContentResponse toResponseDto(OpinionImageContent opinionImageContent);
}
