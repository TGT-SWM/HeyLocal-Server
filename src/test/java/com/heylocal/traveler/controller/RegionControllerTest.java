package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

class RegionControllerTest {
  @Mock
  private RegionService regionService;
  private RegionController regionController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    regionController = new RegionController(regionService);
  }

  @Test
  @DisplayName("State 관련 Region 조회 핸들러")
  void getRegionsTest() throws NotFoundException {
    //GIVEN
    long notExistRegionId = -1;
    long regionId = 260;

    //Mock 행동 정의 - regionService
    willThrow(NotFoundException.class).given(regionService).inquiryRegions(notExistRegionId);

    //WHEN
    List<RegionDto.RegionResponse> succeedResult = regionController.getRegions(regionId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 정상 id 로 요청 시
        () -> assertDoesNotThrow(() -> regionController.getRegions(regionId)),
        //성공 케이스 - 2 - 빈 state 로 요청 시
        () -> {
          regionController.getRegions(null);
          then(regionService).should(times(1)).inquiryAllRegions();
        },
        //실패 케이스 - 1 - 존재하지 않는 id 으로 요청 시
        () -> assertThrows(NotFoundException.class, () -> regionController.getRegions(notExistRegionId))
    );
  }
}