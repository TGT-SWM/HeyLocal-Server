package com.heylocal.traveler.code.mapstruct.multi;

import com.heylocal.traveler.code.mapstruct.Entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MultiEntityMapper {
  MultiEntityMapper INSTANCE = Mappers.getMapper(MultiEntityMapper.class);

  @Mapping(target = "fieldA", source = "entityA.fieldA")
  @Mapping(target = "fieldB", source = "entityA.fieldB")
  @Mapping(target = "otherFieldA", source = "entityB.fieldA")
  @Mapping(target = "otherFieldB", source = "entityB.fieldB")
  MultiEntityDto toDto(Entity entityA, OtherEntity entityB);
}
