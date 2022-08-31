package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.*;

@Import(RegionRepository.class)
@DataJpaTest
class RegionRepositoryTest {
  @Autowired
  private EntityManager em;
  @Autowired
  private RegionRepository regionRepository;

  @Test
  @DisplayName("State 와 City로 Region 엔티티 조회 성공")
  void findByStateAndCitySucceedTest() {
    //GIVEN
    Region region = Region.builder()
        .city("성남시")
        .state("경기도")
        .build();
    em.persist(region);

    //WHEN
    Region result = regionRepository.findByStateAndCity(region.getState(), region.getCity());

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회 성공
        () -> assertDoesNotThrow(
            () -> regionRepository.findByStateAndCity(region.getState(), region.getCity())
        ),
        //성공 케이스 - 2 - 조회결과 확인
        () -> assertEquals(region, result),
        //성공 케이스 - 3 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("존재하지 않는 State 와 City로 Region 엔티티 조회")
  void findByStateAndCityNotExistRegionTest() {
    //GIVEN
    String wrongState = "없는 시/도";
    String wrongCity = "없는 도시";

    //WHEN

    //THEN
    assertAll(
        //실패 케이스 - 1 - 조회 성공
        () -> assertThrows(
            NoResultException.class,
            () -> regionRepository.findByStateAndCity(wrongState, wrongCity)
        ),
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }
}