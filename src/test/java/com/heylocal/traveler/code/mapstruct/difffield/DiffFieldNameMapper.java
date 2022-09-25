package com.heylocal.traveler.code.mapstruct.difffield;

import com.heylocal.traveler.code.mapstruct.Entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DiffFieldNameMapper {
  DiffFieldNameMapper INSTANCE = Mappers.getMapper(DiffFieldNameMapper.class);

  @Mapping(target = "newFieldA", source = "fieldA")
  @Mapping(target = "newFieldB", source = "fieldB")
  DiffFieldNameDto toDto(Entity entity);
}
