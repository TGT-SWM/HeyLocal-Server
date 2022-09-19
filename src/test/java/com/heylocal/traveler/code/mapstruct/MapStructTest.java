package com.heylocal.traveler.code.mapstruct;

import com.heylocal.traveler.code.mapstruct.basic.BasicDto;
import com.heylocal.traveler.code.mapstruct.basic.BasicMapper;
import com.heylocal.traveler.code.mapstruct.defaultmethod.DefaultMethodMapper;
import com.heylocal.traveler.code.mapstruct.defaultvalue.DefaultValueMapper;
import com.heylocal.traveler.code.mapstruct.dependent.*;
import com.heylocal.traveler.code.mapstruct.difffield.DiffFieldNameDto;
import com.heylocal.traveler.code.mapstruct.difffield.DiffFieldNameMapper;
import com.heylocal.traveler.code.mapstruct.enums.DetailFruitType;
import com.heylocal.traveler.code.mapstruct.enums.EnumMapper;
import com.heylocal.traveler.code.mapstruct.enums.GeneralFruitType;
import com.heylocal.traveler.code.mapstruct.exception.ExceptionMapper;
import com.heylocal.traveler.code.mapstruct.expression.ExpressionMapper;
import com.heylocal.traveler.code.mapstruct.inherit.InheritMapper;
import com.heylocal.traveler.code.mapstruct.inherit.MyDto;
import com.heylocal.traveler.code.mapstruct.inherit.MyEntity;
import com.heylocal.traveler.code.mapstruct.multi.MultiEntityDto;
import com.heylocal.traveler.code.mapstruct.multi.MultiEntityMapper;
import com.heylocal.traveler.code.mapstruct.multi.OtherEntity;
import com.heylocal.traveler.code.mapstruct.type.LocalDateDto;
import com.heylocal.traveler.code.mapstruct.type.LocalDateEntity;
import com.heylocal.traveler.code.mapstruct.type.LocalDateMapper;
import com.heylocal.traveler.code.mapstruct.update.EntityUpdateMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class MapStructTest {

  @Test
  @DisplayName("기본 매퍼 사용")
  void useBasicMapper() {
    //GIVEN
    Entity entity = new Entity(1L, "fieldA Value", "fieldB Value");

    //WHEN
    BasicDto dto = BasicMapper.INSTANCE.toDto(entity);

    //THEN
    assertAll(
        () -> assertSame(entity.getId(), dto.getId()),
        () -> assertEquals(entity.getFieldA(), dto.getFieldA()),
        () -> assertEquals(entity.getFieldB(), dto.getFieldB())
    );
  }

  @Test
  @DisplayName("DTO 와 엔티티의 필드명이 다른 경우")
  void differentFieldName() {
    //GIVEN
    Entity entity = new Entity(1L, "fieldA Value", "fieldB Value");

    //WHEN
    DiffFieldNameDto dto = DiffFieldNameMapper.INSTANCE.toDto(entity);

    //THEN
    assertAll(
        () -> assertSame(entity.getId(), dto.getId()),
        () -> assertEquals(entity.getFieldA(), dto.getNewFieldA()),
        () -> assertEquals(entity.getFieldB(), dto.getNewFieldB())
    );
  }

  @Test
  @DisplayName("한 DTO에 여러 엔티티를 매핑하는 경우")
  void multiEntity() {
    //GIVEN
    Entity entityA = new Entity(1L, "entityA.fieldA Value", "entityA.fieldB Value");
    OtherEntity entityB = new OtherEntity(1L, "entityB.fieldA Value", "entityB.fieldB Value");

    //WHEN
    MultiEntityDto dto = MultiEntityMapper.INSTANCE.toDto(entityA, entityB);

    //THEN
    assertAll(
        () -> assertEquals(entityA.getFieldA(), dto.getFieldA()),
        () -> assertEquals(entityA.getFieldB(), dto.getFieldB()),
        () -> assertEquals(entityB.getFieldA(), dto.getOtherFieldA()),
        () -> assertEquals(entityB.getFieldB(), dto.getOtherFieldB())
    );
  }

  @Test
  @DisplayName("다른 엔티티를 의존하는 엔티티를 매핑하는 경우")
  void dependentEntity() {
    //GIVEN
    Entity entityA = new Entity(1L, "entityA.fieldA Value", "entityA.fieldB Value");
    OtherEntity entityB1 = new OtherEntity(1L, "entityB1.fieldA Value", "entityB1.fieldB Value");
    OtherEntity entityB2 = new OtherEntity(2L, "entityB2.fieldA Value", "entityB2.fieldB Value");
    List<OtherEntity> otherEntityList = new ArrayList<>();
    otherEntityList.add(entityB1);
    otherEntityList.add(entityB2);

    DependentOtherEntity dependentOtherEntity = new DependentOtherEntity(
        1L,
        "dependentOtherEntity.fieldA Value",
        entityA,
        otherEntityList
    );

    //WHEN
    DependentOtherDto dto = DependentOtherMapper.INSTANCE.toDto(dependentOtherEntity);
    EntityDto entityADto = dto.getEntityDto();
    List<OtherEntityDto> entityBDtoList = dto.getOtherEntityDtoList();
    OtherEntityDto entityB1Dto = entityBDtoList.get(0);
    OtherEntityDto entityB2Dto = entityBDtoList.get(1);

    //THEN
    assertAll(
        () -> assertEquals(entityA.getFieldA(), entityADto.getFieldA()),
        () -> assertEquals(entityA.getFieldB(), entityADto.getFieldB()),
        () -> assertEquals(entityB1.getFieldA(), entityB1Dto.getFieldA()),
        () -> assertEquals(entityB1.getFieldB(), entityB1Dto.getFieldB()),
        () -> assertEquals(entityB2.getFieldA(), entityB2Dto.getFieldA()),
        () -> assertEquals(entityB2.getFieldB(), entityB2Dto.getFieldB())
    );
  }

  @Test
  @DisplayName("기존 엔티티 업데이트")
  void updateEntity() {
    //GIVEN
    /* 기존 엔티티 */
    DependentOtherEntity existedEntity = new DependentOtherEntity(
        1L,
        "old fieldA Value",
        new Entity(1L, "", ""),
        new ArrayList<>()
    );

    List<OtherEntityDto> otherEntityList = new ArrayList<>();
    otherEntityList.add(new OtherEntityDto(1L, "new value", "new value"));
    otherEntityList.add(new OtherEntityDto(2L, "new value", "new value"));
    /* 새로운 데이터(DTO) */
    DependentOtherDto newDto = new DependentOtherDto(
        "new fieldA value",
        new EntityDto(5L, "new value", "new value"),
        otherEntityList
    );

    //WHEN
    EntityUpdateMapper.INSTANCE.updateEntity(newDto, existedEntity);

    //THEN
    assertAll(
        () -> assertEquals(newDto.getFieldA(), existedEntity.getFieldA()),
        () -> assertEquals(newDto.getEntityDto().getId(), existedEntity.getEntity().getId()),
        () -> assertSame(2, existedEntity.getOtherEntityList().size())
    );
  }

  @Test
  @DisplayName("Enum 간 매핑 - 첫번째 방법")
  void enumMapping1() {
    //GIVEN
    DetailFruitType orangeDetailType = DetailFruitType.ORANGE;
    DetailFruitType bananaDetailType = DetailFruitType.BANANA;
    DetailFruitType redAppleDetailType = DetailFruitType.RED_APPLE;
    DetailFruitType yellowAppleDetailType = DetailFruitType.YELLOW_APPLE;
    DetailFruitType greenAppleDetailType = DetailFruitType.GREEN_APPLE;

    //WHEN
    GeneralFruitType orangeResult = EnumMapper.INSTANCE.toFruitType1(orangeDetailType);
    GeneralFruitType bananaResult = EnumMapper.INSTANCE.toFruitType1(bananaDetailType);
    GeneralFruitType redAppleResult = EnumMapper.INSTANCE.toFruitType1(redAppleDetailType);
    GeneralFruitType yellowAppleResult = EnumMapper.INSTANCE.toFruitType1(yellowAppleDetailType);
    GeneralFruitType greenAppleResult = EnumMapper.INSTANCE.toFruitType1(greenAppleDetailType);

    //THEN
    assertAll(
        () -> assertSame(GeneralFruitType.ORANGE, orangeResult),
        () -> assertSame(GeneralFruitType.BANANA, bananaResult),
        () -> assertSame(GeneralFruitType.APPLE, redAppleResult),
        () -> assertSame(GeneralFruitType.APPLE, yellowAppleResult),
        () -> assertSame(GeneralFruitType.APPLE, greenAppleResult)
    );
  }

  @Test
  @DisplayName("Enum 간 매핑 - 두번째 방법")
  void enumMapping2() {
    //GIVEN
    DetailFruitType orangeDetailType = DetailFruitType.ORANGE;
    DetailFruitType bananaDetailType = DetailFruitType.BANANA;
    DetailFruitType redAppleDetailType = DetailFruitType.RED_APPLE;
    DetailFruitType yellowAppleDetailType = DetailFruitType.YELLOW_APPLE;
    DetailFruitType greenAppleDetailType = DetailFruitType.GREEN_APPLE;

    //WHEN
    GeneralFruitType orangeResult = EnumMapper.INSTANCE.toFruitType2(orangeDetailType);
    GeneralFruitType bananaResult = EnumMapper.INSTANCE.toFruitType2(bananaDetailType);
    GeneralFruitType redAppleResult = EnumMapper.INSTANCE.toFruitType2(redAppleDetailType);
    GeneralFruitType yellowAppleResult = EnumMapper.INSTANCE.toFruitType2(yellowAppleDetailType);
    GeneralFruitType greenAppleResult = EnumMapper.INSTANCE.toFruitType2(greenAppleDetailType);

    //THEN
    assertAll(
        () -> assertSame(GeneralFruitType.ORANGE, orangeResult),
        () -> assertSame(GeneralFruitType.BANANA, bananaResult),
        () -> assertSame(GeneralFruitType.APPLE, redAppleResult),
        () -> assertSame(GeneralFruitType.APPLE, yellowAppleResult),
        () -> assertSame(GeneralFruitType.APPLE, greenAppleResult)
    );
  }

  @Test
  @DisplayName("LocalDate 값을 String 타입에 매핑")
  void localDateMapper() {
    //GIVEN
    LocalDateEntity entity = new LocalDateEntity(1L, LocalDate.now());

    //WHEN
    LocalDateDto dto = LocalDateMapper.INSTANCE.toDto(entity);
    boolean matchResult = Pattern.matches("^[0-9]{4}/[0-9]{2}/[0-9]{2}$", dto.getDate());

    //THEN
    assertAll(
        () -> assertSame(entity.getId(), dto.getId()),
        () -> assertTrue(matchResult)
    );
  }

  @Test
  @DisplayName("default method 를 통한 매핑 메서드 직접 구현")
  void defaultMethodMapper() {
    //GIVEN
    Entity entity = new Entity(1L, "valueA", "valueB");

    //WHEN
    EntityDto basicDto = DefaultMethodMapper.INSTANCE.basicToDto(entity);
    EntityDto specialDto = DefaultMethodMapper.INSTANCE.specialToDto(entity);

    //THEN
    assertAll(
        () -> assertSame(entity.getId(), basicDto.getId()),
        () -> assertEquals(entity.getFieldA(), basicDto.getFieldA()),
        () -> assertEquals(entity.getFieldB(), basicDto.getFieldB()),
        () -> assertSame(entity.getId(), basicDto.getId()),
        () -> assertEquals("Special Value of A!!!", specialDto.getFieldA()),
        () -> assertEquals("Special Value of B!!!", specialDto.getFieldB())
    );
  }

  @Test
  @DisplayName("필드에 기본값 매핑")
  void abstractMapper() {
    //GIVEN
    Entity entity = new Entity(10L, null, null);

    //WHEN
    EntityDto dto = DefaultValueMapper.INSTANCE.toDto(entity);

    //THEN
    assertAll(
        () -> assertSame(-1L, dto.getId()),
        () -> assertEquals("empty value", dto.getFieldA()),
        () -> assertNull(dto.getFieldB())
    );
  }

  @Test
  @DisplayName("자바 표현식으로 매핑")
  void expressionMapper() {
    //GIVEN
    Entity entity = new Entity(10L, null, "fieldB value");

    //WHEN
    EntityDto dto = ExpressionMapper.INSTANCE.toDto(entity);
    log.info("dto.id = {}", dto.getId());
    log.info("dto.fieldA = {}", dto.getFieldA());
    log.info("dto.fieldB = {}", dto.getFieldB());

    //THEN
    assertAll(
        () -> assertNotSame(entity.getId(), dto.getId()),
        () -> assertNotNull(dto.getFieldA()),
        () -> assertEquals("fieldB value", dto.getFieldB())
    );
  }

  @Test
  @DisplayName("예외처리와 함께 매핑")
  void exceptionMapper() {
    //GIVEN
    Entity invalidIdEntity = new Entity(-10L, "fieldA value", "fieldB value");
    Entity invalidFieldEntity1 = new Entity(1L, "", "fieldB value");
    Entity invalidFieldEntity2 = new Entity(1L, "fieldA value", "");
    Entity validEntity = new Entity(1L, "fieldA value", "fieldB value");

    //WHEN
    //ExceptionMapper.INSTANCE.toDto(invalidIdEntity);
    //ExceptionMapper.INSTANCE.toDto(invalidFieldEntity1);
    //ExceptionMapper.INSTANCE.toDto(invalidFieldEntity2)
    ExceptionMapper.INSTANCE.toDto(validEntity);

    //THEN
    assertAll(
        () -> assertThrows(
            IllegalArgumentException.class,
            () -> ExceptionMapper.INSTANCE.toDto(invalidIdEntity)
        ),
        () -> assertThrows(
            IllegalArgumentException.class,
            () -> ExceptionMapper.INSTANCE.toDto(invalidFieldEntity1)
        ),
        () -> assertThrows(
            IllegalArgumentException.class,
            () -> ExceptionMapper.INSTANCE.toDto(invalidFieldEntity2)
        ),
        () -> assertDoesNotThrow(
            () -> ExceptionMapper.INSTANCE.toDto(validEntity)
        )
    );
  }

  @Test
  @DisplayName("@InheritConfiguration 을 통한 매핑설정 상속")
  void inheritConfiguration() {
    //GIVEN
    MyDto dto = new MyDto(1L, 1, 1);

    //WHEN
    MyEntity entity = InheritMapper.INSTANCE.toEntity(dto);
    dto.setId(10L);
    InheritMapper.INSTANCE.updateEntity(dto, entity);

    //THEN
    assertSame(dto.getId(), entity.getId());
  }

  @Test
  @DisplayName("@InheritInverseConfiguration 을 통한 매핑설정 상속")
  void inheritInverseConfiguration() {
    //GIVEN
    MyEntity entity = new MyEntity(1L, 1, 1);

    //WHEN
    MyDto dto = InheritMapper.INSTANCE.toDto(entity);

    //THEN
    assertAll(
        () -> assertSame(dto.getId(), entity.getId()),
        () -> assertSame(dto.getNewMyFieldA(), entity.getMyFieldA()),
        () -> assertSame(dto.getNewMyFieldB(), entity.getMyFieldB())
    );
  }
}
