/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : TravelMemberMapper
 * author         : 우태균
 * date           : 2022/09/19
 * description    : TravelMember 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.domain.travelon.list.TravelMember;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.TravelMemberDto.TravelMemberResponse;

@Mapper(builder = @Builder(disableBuilder = true))
public interface TravelMemberMapper {
  TravelMemberMapper INSTANCE = Mappers.getMapper(TravelMemberMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "memberType", source = "type")
  TravelMember toEntity(TravelOn travelOn, MemberType type);

  @Mapping(target = "type", source = "memberType")
  TravelMemberResponse toResponseDto(TravelMember travelMember);

  @AfterMapping
  default void registerTravelOnToEntity(TravelOn travelOn, @MappingTarget TravelMember travelMember) {
    travelMember.registerAt(travelOn);
  }
}
