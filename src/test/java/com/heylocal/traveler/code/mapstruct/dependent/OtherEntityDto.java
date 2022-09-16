package com.heylocal.traveler.code.mapstruct.dependent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherEntityDto {
  private long id;
  private String fieldA;
  private String fieldB;
}
