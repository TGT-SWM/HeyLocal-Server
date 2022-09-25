package com.heylocal.traveler.code.mapstruct.inherit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyDto {
  private long id;
  private int newMyFieldA; //MyEntity의 myFieldA 와 매핑되어야 함.
  private int newMyFieldB; //MyEntity의 myFieldB 와 매핑되어야 함.
}
