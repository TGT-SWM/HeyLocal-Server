package com.heylocal.traveler.code.mapstruct.inherit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyEntity {
  private Long id;
  private int myFieldA;
  private int myFieldB;
}
