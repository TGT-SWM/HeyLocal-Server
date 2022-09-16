package com.heylocal.traveler.code.mapstruct.defaultvalue;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DefaultValueMapper {
  DefaultValueMapper INSTANCE = Mappers.getMapper(DefaultValueMapper.class);

  @Mapping(target = "id", constant = "-1L")
  @Mapping(target = "fieldA", defaultValue = "empty value")
  EntityDto toDto(Entity entity);
}
