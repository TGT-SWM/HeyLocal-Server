package com.heylocal.traveler.code.mapstruct.dependent;

import com.heylocal.traveler.code.mapstruct.multi.OtherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OtherEntityMapper {
  OtherEntityMapper INSTANCE = Mappers.getMapper(OtherEntityMapper.class);

  OtherEntityDto toDto(OtherEntity entity);
}
