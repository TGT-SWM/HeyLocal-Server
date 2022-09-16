package com.heylocal.traveler.code.mapstruct.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalDateEntity {
  private Long id;
  private LocalDate localDate;
}
