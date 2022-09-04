package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
    Region result = regionRepository.findByStateAndCity(region.getState(), region.getCity()).get();

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
    Optional<Region> result = regionRepository.findByStateAndCity(wrongState, wrongCity);

    //THEN
    assertAll(
        //실패 케이스 - 1 - 조회 실패
        () -> assertTrue(result.isEmpty()),
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("state 로 Region 리스트 조회 성공")
  void findByStateSucceedTest() {
    //GIVEN
    String storedStateA = "stateA";
    String storedCity1 = "city1";
    Region storedRegion1 = Region.builder()
        .state(storedStateA)
        .city(storedCity1)
        .build();
    String storedCity2 = "city2";
    Region storedRegion2 = Region.builder()
        .state(storedStateA)
        .city(storedCity2)
        .build();
    String unknownState = "stateB";

    em.persist(storedRegion1);
    em.persist(storedRegion2);

    //WHEN
    List<Region> succeedResult = regionRepository.findByState(storedStateA);
    List<Region> failResult = regionRepository.findByState(unknownState);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 state 로 조회 시
        () -> assertEquals(2, succeedResult.size()),
        //실패 케이스 - 1 - 존재하지 않는 state 로 조회 시
        () -> assertEquals(0, failResult.size())
    );
  }
}