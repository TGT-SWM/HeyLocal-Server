package com.heylocal.traveler.code.mapstruct.dependent;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EntityMapper.class, OtherEntityMapper.class})
public interface DependentOtherMapper {
  DependentOtherMapper INSTANCE = Mappers.getMapper(DependentOtherMapper.class);

  @Mapping(target = "entityDto", source = "dependentEntity.entity")
  @Mapping(target = "otherEntityDtoList", source = "dependentEntity.otherEntityList")
  DependentOtherDto toDto(DependentOtherEntity dependentEntity);
}
