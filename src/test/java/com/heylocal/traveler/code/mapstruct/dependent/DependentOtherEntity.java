package com.heylocal.traveler.code.mapstruct.dependent;

import com.heylocal.traveler.code.mapstruct.Entity;
import com.heylocal.traveler.code.mapstruct.multi.OtherEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DependentOtherEntity {
  private Long id;
  private String fieldA;
  private Entity entity; //ManyToOne 관계
  private List<OtherEntity> otherEntityList; //OneToMany 관계
}
