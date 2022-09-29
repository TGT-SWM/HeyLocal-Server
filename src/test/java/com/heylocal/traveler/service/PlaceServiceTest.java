package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class PlaceServiceTest {
  @Mock
  private PlaceRepository placeRepository;
  private PlaceService placeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    placeService = new PlaceService(placeRepository);
  }

  @Test
  @DisplayName("ID로 장소 조회")
  void inquiryPlaceTest() throws NotFoundException {
    //GIVEN
    long existPlaceId = 1L;
    long notExistPlaceId = 2L;
    String placeName = "my place";
    Place savedPlace = Place.builder()
        .id(existPlaceId)
        .name(placeName)
        .build();

    //Mock 행동 정의 - placeRepository
    willReturn(Optional.of(savedPlace)).given(placeRepository).findById(existPlaceId);
    willReturn(Optional.empty()).given(placeRepository).findById(notExistPlaceId);

    //WHEN
    PlaceDto.PlaceResponse result = placeService.inquiryPlace(existPlaceId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 장소 ID로 조회한 경우
        () -> assertEquals(placeName, result.getName()),
        //실패 케이스 - 1 - 존재하지 않는 장소 ID로 조회한 경우
        () -> assertThrows(NotFoundException.class, () -> placeService.inquiryPlace(notExistPlaceId))
    );
  }
}