/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : OpinionImageContentMapper
 * author         : 우태균
 * date           : 2022/09/23
 * description    : OpinionImageContent 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.OpinionImageContentDto.OpinionImageContentResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;

@Mapper(builder = @Builder(disableBuilder = true))
public interface OpinionImageContentMapper {
  OpinionImageContentMapper INSTANCE = Mappers.getMapper(OpinionImageContentMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "opinion", ignore = true)
  @Mapping(target = "objectKeyName", source = "objectKeyName")
  @Mapping(target = "imageContentType", source = "imageContentType")
  OpinionImageContent toEntity(String objectKeyName, ImageContentType imageContentType, Opinion opinion);

  OpinionImageContentResponse toResponseDto(OpinionImageContent opinionImageContent);

  @AfterMapping
  default void registerOpinion(Opinion opinion, @MappingTarget OpinionImageContent opinionImageContent) {
    opinionImageContent.setOpinion(opinion);
  }
}
