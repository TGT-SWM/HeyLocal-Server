package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.OpinionImageContentDto;
import com.heylocal.traveler.dto.OpinionImageContentDto.OpinionImageContentResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.dto.OpinionImageContentDto.*;

@Mapper(builder = @Builder(disableBuilder = true))
public interface OpinionImageContentMapper {
  OpinionImageContentMapper INSTANCE = Mappers.getMapper(OpinionImageContentMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "opinion", ignore = true)
  OpinionImageContent toEntity(String objectKeyName, ImageContentType imageContentType, Opinion opinion);

  OpinionImageContentResponse toResponseDto(OpinionImageContent opinionImageContent);

  @AfterMapping
  default void registerOpinion(Opinion opinion, @MappingTarget OpinionImageContent opinionImageContent) {
    opinionImageContent.setOpinion(opinion);
  }
}
