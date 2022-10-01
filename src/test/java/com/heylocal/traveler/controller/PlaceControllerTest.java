package com.heylocal.traveler.controller;

import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionService;
import com.heylocal.traveler.service.PlaceService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import static com.heylocal.traveler.dto.PageDto.PageRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class PlaceControllerTest {
  @Mock
  private BindingErrorMessageProvider errorMessageProvider;
  @Mock
  private PlaceService placeService;
  @Mock
  private OpinionService opinionService;
  @Mock
  private BindingResult bindingResult;
  private PlaceController placeController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    placeController = new PlaceController(errorMessageProvider, placeService, opinionService);
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

  @Test
  @DisplayName("특정 장소에 대한 답변 목록 조회")
  void getPlaceOpinionsTest() throws BadRequestException {
    //GIVEN
    long placeId = 1L;
    PageRequest invalidPageRequest = PageRequest.builder().lastItemId(null).size(0).build();

    //WHEN
    willReturn(true).given(bindingResult).hasFieldErrors();

    //THEN
    //실패 케이스 - 페이지 사이즈가 0 이하인 경우
    assertThrows(BadRequestException.class, () -> placeController.getPlaceOpinions(placeId, invalidPageRequest, bindingResult));
  }

  @Test
  @DisplayName("인기 장소 조회 핸들러")
  void getHotPlacesTest() {
    //GIVEN
    long placeId = 1L;
    PageRequest validPageRequest = PageRequest.builder().lastItemId(null).size(3).build();
    PageRequest invalidPageRequest = PageRequest.builder().lastItemId(null).size(0).build();

    //Mock 행동 정의 - bindingResult
    willReturn(false).willReturn(true).given(bindingResult).hasFieldErrors();

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 페이징 요청 정상 값
        () -> assertDoesNotThrow(() -> placeController.getPlaceOpinions(placeId, validPageRequest, bindingResult)),
        //실패 케이스 - 1 - 페이징 요청 비정상 값
        () -> assertThrows(BadRequestException.class, () -> placeController.getPlaceOpinions(placeId, validPageRequest, bindingResult))
    );
  }
}