package com.heylocal.traveler.code.mapstruct.abstractmapper;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;

@Mapper
public abstract class AbstractMapper {

  //기존 DefaultMethodMapper 인터페이스의 메서드와 동일
  public abstract EntityDto basicToDto(Entity entity);

  //기존 DefaultMethodMapper 인터페이스의 default 메서드와 동일
  public EntityDto specialToDto(Entity entity) {
    EntityDto dto = new EntityDto();

    dto.setId(entity.getId());
    dto.setFieldA("Special Value of A!!!");
    dto.setFieldB("Special Value of B!!!");

    return dto;
  }

}
