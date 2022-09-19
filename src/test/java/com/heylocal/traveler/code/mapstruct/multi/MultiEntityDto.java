package com.heylocal.traveler.code.mapstruct.multi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiEntityDto {
  private String fieldA; //Entity 클래스의 fieldA 와 매핑
  private String fieldB; //Entity 클래스의 fieldB 와 매핑
  private String otherFieldA; //OtherEntity 클래스의 fieldA 와 매핑
  private String otherFieldB; //OtherEntity 클래스의 fieldB 와 매핑
}
