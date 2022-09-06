package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

class RegionServiceTest {
  @Mock
  private RegionRepository regionRepository;
  private RegionService regionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    regionService = new RegionService(regionRepository);
  }

  @Test
  @DisplayName("state 으로 Region 조회")
  void inquiryRegionsTest() throws BadArgumentException {
    //GIVEN
    String existState = "existState";
    String notExistState = "notExistState";

    //Mock 행동 정의 - RegionRepository
    Region region1 = Region.builder()
            .id(1L)
            .state(existState)
            .city("existCity1")
            .build();
    Region region2 = Region.builder()
        .id(2L)
        .state(existState)
        .city("existCity2")
        .build();
    List<Region> regionList = new ArrayList<>();
    regionList.add(region1);
    regionList.add(region2);
    willReturn(regionList).given(regionRepository).findByState(eq(existState));
    willReturn(new ArrayList<Region>()).given(regionRepository).findByState(eq(notExistState));

    //WHEN
    List<RegionDto.RegionResponse> succeedResult = regionService.inquiryRegions(existState);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 state 조회시
        () -> assertDoesNotThrow(() -> regionService.inquiryRegions(existState)),
        () -> assertEquals(2, succeedResult.size()),
        //실패 케이스 - 1 - 존재하지 않는 state 조회시
        () -> assertThrows(BadArgumentException.class, () -> regionService.inquiryRegions(notExistState))
    );
  }
}