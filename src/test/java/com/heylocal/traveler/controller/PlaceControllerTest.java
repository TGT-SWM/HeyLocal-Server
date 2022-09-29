package com.heylocal.traveler.controller;

import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willThrow;

class PlaceControllerTest {
  @Mock
  private PlaceService placeService;
  private PlaceController placeController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    placeController = new PlaceController(placeService);
  }

  @Test
  @DisplayName("장소 정보 조회 핸들러")
  void getPlaceTest() throws NotFoundException {
    //GIVEN
    long existPlaceId = 1L;
    long notExistPlaceId = 2L;

    //Mock 행동 정의 - placeService
    willThrow(NotFoundException.class).given(placeService).inquiryPlace(notExistPlaceId);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 Place ID 로 조회시
        () -> assertDoesNotThrow(() -> placeController.getPlace(existPlaceId)),
        //실패 케이스 - 1 - 존재하지 않는 Place ID 로 조회시
        () -> assertThrows(NotFoundException.class, () -> placeController.getPlace(notExistPlaceId))
    );
  }
}