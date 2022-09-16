package com.heylocal.traveler.code.mapstruct.defaultmethod;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DefaultMethodMapper {
  DefaultMethodMapper INSTANCE = Mappers.getMapper(DefaultMethodMapper.class);

  //MapStruct 에서 생성해주는 매핑 메서드
  EntityDto basicToDto(Entity entity);

  //직접 작성한 매핑 메서드
  default EntityDto specialToDto(Entity entity) {
    EntityDto dto = new EntityDto();

    dto.setId(entity.getId());
    dto.setFieldA("Special Value of A!!!");
    dto.setFieldB("Special Value of B!!!");

    return dto;
  }
}
