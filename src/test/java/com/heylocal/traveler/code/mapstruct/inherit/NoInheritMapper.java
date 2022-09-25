package com.heylocal.traveler.code.mapstruct.inherit;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoInheritMapper {
  NoInheritMapper INSTANCE = Mappers.getMapper(NoInheritMapper.class);

  @Mapping(target = "myFieldA", source = "newMyFieldA")
  @Mapping(target = "myFieldB", source = "newMyFieldB")
  MyEntity toEntity(MyDto dto);

  //MtDto 값으로 MyEntity 업데이트
  @Mapping(target = "myFieldA", source = "newMyFieldA")
  @Mapping(target = "myFieldB", source = "newMyFieldB")
  void updateEntity(MyDto dto, @MappingTarget MyEntity entity);

  @Mapping(target = "newMyFieldA", source = "myFieldA")
  @Mapping(target = "newMyFieldB", source = "myFieldB")
  MyDto toDto(MyEntity entity);
}
