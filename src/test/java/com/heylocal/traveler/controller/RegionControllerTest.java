package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willThrow;

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
  void getRegionsTest() throws BadArgumentException, NotFoundException {
    //GIVEN
    String existState = "exitState";
    String notExistState = "notExistState";

    //Mock 행동 정의 - regionService
    willThrow(BadArgumentException.class).given(regionService).inquiryRegions(eq(notExistState));

    //WHEN
    List<RegionDto.RegionResponse> succeedResult = regionController.getRegions(existState);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 정상 state 으로 요청 시
        () -> assertDoesNotThrow(() -> regionController.getRegions(existState)),
        //실패 케이스 - 1 - 존재하지 않는 state 으로 요청 시
        () -> assertThrows(NotFoundException.class, () -> regionController.getRegions(notExistState))
    );
  }
}