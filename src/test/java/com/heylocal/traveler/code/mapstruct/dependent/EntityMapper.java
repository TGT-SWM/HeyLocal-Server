package com.heylocal.traveler.code.mapstruct.dependent;

import com.heylocal.traveler.code.mapstruct.Entity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {
  EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

  EntityDto toDto(Entity entity);
}
