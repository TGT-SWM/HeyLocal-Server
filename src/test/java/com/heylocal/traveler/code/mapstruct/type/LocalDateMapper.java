package com.heylocal.traveler.code.mapstruct.type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocalDateMapper {
  LocalDateMapper INSTANCE = Mappers.getMapper(LocalDateMapper.class);

  @Mapping(target = "date", source = "localDate", dateFormat = "yyyy/MM/dd")
  LocalDateDto toDto(LocalDateEntity entity);
}
