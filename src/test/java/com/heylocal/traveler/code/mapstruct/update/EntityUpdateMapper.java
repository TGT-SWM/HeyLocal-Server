package com.heylocal.traveler.code.mapstruct.update;

import com.heylocal.traveler.code.mapstruct.dependent.DependentOtherDto;
import com.heylocal.traveler.code.mapstruct.dependent.DependentOtherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityUpdateMapper {
  EntityUpdateMapper INSTANCE = Mappers.getMapper(EntityUpdateMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entity", source = "dto.entityDto")
  @Mapping(target = "otherEntityList", source = "dto.otherEntityDtoList")
  void updateEntity(DependentOtherDto dto, @MappingTarget DependentOtherEntity oldEntity);
}
