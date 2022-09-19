package com.heylocal.traveler.code.mapstruct.enums;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EnumMapper {
  EnumMapper INSTANCE = Mappers.getMapper(EnumMapper.class);

  //매핑하는 첫번째 방법
  @ValueMappings({
      @ValueMapping(target = "APPLE", source = "RED_APPLE"),
      @ValueMapping(target = "APPLE", source = "YELLOW_APPLE"),
      @ValueMapping(target = "APPLE", source = "GREEN_APPLE")
  })
  GeneralFruitType toFruitType1(DetailFruitType detailFruitType);

  //매핑하는 두번째 방법
  @ValueMapping(target = "APPLE", source = MappingConstants.ANY_REMAINING)
  GeneralFruitType toFruitType2(DetailFruitType detailFruitType);
}
