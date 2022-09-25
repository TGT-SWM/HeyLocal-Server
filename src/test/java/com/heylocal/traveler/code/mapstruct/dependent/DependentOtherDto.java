package com.heylocal.traveler.code.mapstruct.dependent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DependentOtherDto {
  private String fieldA;
  private EntityDto entityDto;
  private List<OtherEntityDto> otherEntityDtoList;
}
