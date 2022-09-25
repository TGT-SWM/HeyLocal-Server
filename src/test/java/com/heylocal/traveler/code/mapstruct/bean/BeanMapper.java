package com.heylocal.traveler.code.mapstruct.bean;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeanMapper {
  /*
   * 아래 코드는 더이상 필요없다.
   * BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);
   */

  EntityDto toDto(Entity entity);

}
