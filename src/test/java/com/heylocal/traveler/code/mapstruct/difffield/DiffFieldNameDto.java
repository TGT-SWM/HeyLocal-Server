package com.heylocal.traveler.code.mapstruct.difffield;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiffFieldNameDto {
  private long id;
  private String newFieldA;
  private String newFieldB;
}
