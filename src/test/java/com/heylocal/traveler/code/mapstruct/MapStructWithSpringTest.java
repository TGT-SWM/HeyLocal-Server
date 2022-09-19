package com.heylocal.traveler.code.mapstruct;

import com.heylocal.traveler.code.mapstruct.bean.BeanMapper;
import com.heylocal.traveler.code.mapstruct.dependent.EntityDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MapStructWithSpringTest {

  //MapperImpl 구체 클래스를 DI 컨테이너로부터 주입받는다.
  @Autowired
  private BeanMapper beanMapper;

  @Test
  @DisplayName("Spring Bean 으로 등록된 매퍼 사용")
  void useBasicMapper() {
    //GIVEN
    Entity entity = new Entity(1L, "fieldA Value", "fieldB Value");

    //WHEN
    EntityDto dto = beanMapper.toDto(entity);

    //THEN
    assertAll(
        () -> assertSame(entity.getId(), dto.getId()),
        () -> assertEquals(entity.getFieldA(), dto.getFieldA()),
        () -> assertEquals(entity.getFieldB(), dto.getFieldB())
    );
  }
}
