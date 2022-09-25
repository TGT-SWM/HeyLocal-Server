package com.heylocal.traveler.code.mapstruct.basic;

import com.heylocal.traveler.code.mapstruct.Entity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BasicMapper {
  BasicMapper INSTANCE = Mappers.getMapper(BasicMapper.class);

  BasicDto toDto(Entity entity);
}
