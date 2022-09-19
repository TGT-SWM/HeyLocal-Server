package com.heylocal.traveler.code.mapstruct.expression;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(imports = {LocalDateTime.class, UUID.class})
public interface ExpressionMapper {
  ExpressionMapper INSTANCE = Mappers.getMapper(ExpressionMapper.class);

  @Mapping(target = "id", expression = "java(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)")
  @Mapping(target = "fieldA", defaultExpression = "java(LocalDateTime.now().toString())")
  EntityDto toDto(Entity entity);
}
