package com.heylocal.traveler.code.mapstruct.exception;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Validator.class)
public interface ExceptionMapper {
  ExceptionMapper INSTANCE = Mappers.getMapper(ExceptionMapper.class);

  EntityDto toDto(Entity entity) throws IllegalArgumentException;
}
