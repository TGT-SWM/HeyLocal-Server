package com.heylocal.traveler.code.mapstruct.inherit;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InheritMapper {
  InheritMapper INSTANCE = Mappers.getMapper(InheritMapper.class);

  @Mapping(target = "myFieldA", source = "newMyFieldA")
  @Mapping(target = "myFieldB", source = "newMyFieldB")
  MyEntity toEntity(MyDto dto);

  //toEntity() 메서드의 @Mapping 설정을 그대로 가져온다.
  @InheritConfiguration(name = "toEntity")
  void updateEntity(MyDto dto, @MappingTarget MyEntity entity);

  //toEntity() 메서드의 @Mapping 설정에서 target과 source를 뒤바꿔 가져온다.
  @InheritInverseConfiguration(name = "toEntity")
  MyDto toDto(MyEntity entity);
}
