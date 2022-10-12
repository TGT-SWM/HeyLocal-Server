/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : TravelTypeGroupMapper
 * author         : 우태균
 * date           : 2022/09/19
 * description    : TravelTypeGroup 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.TravelTypeGroup;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface TravelTypeGroupMapper {
  TravelTypeGroupMapper INSTANCE = Mappers.getMapper(TravelTypeGroupMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "travelOn", ignore = true)
  TravelTypeGroup toEntity(TravelTypeGroupRequest travelTypeGroupRequest);

  @Mapping(target = "id", ignore = true)
  TravelTypeGroup toEntity(TravelTypeGroupRequest travelTypeGroupRequest, TravelOn travelOn);

  TravelTypeGroupResponse toResponseDto(TravelTypeGroup travelTypeGroup);

  @AfterMapping
  default void registerTravelOnToEntity(TravelOn travelOn, @MappingTarget TravelTypeGroup travelTypeGroup) {
    travelTypeGroup.registerAt(travelOn);
  }
}
