package com.heylocal.traveler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.util.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import static com.heylocal.traveler.dto.PlaceDto.PlaceWithOpinionSizeResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class PlaceServiceTest {
  @Mock
  private PlaceRepository placeRepository;
  @Mock
  private HttpClient httpClient;
  @Mock
  private ObjectMapper objectMapper;
  private PlaceService placeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    placeService = new PlaceService(placeRepository, httpClient, objectMapper);
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
    PlaceResponse result = placeService.inquiryPlace(existPlaceId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 장소 ID로 조회한 경우
        () -> assertEquals(placeName, result.getName()),
        //실패 케이스 - 1 - 존재하지 않는 장소 ID로 조회한 경우
        () -> assertThrows(NotFoundException.class, () -> placeService.inquiryPlace(notExistPlaceId))
    );
  }

  @Test
  @DisplayName("가장 많은 답변으로 선택된 장소를 조회")
  void inquiryMostOpinedPlaceTest() {
    //GIVEN
    List<Place> findResultWith5Items = new ArrayList<>();
    long placeId1 = 1L;
    findResultWith5Items.add(Place.builder().id(placeId1).build());
    long placeId2 = 2L;
    findResultWith5Items.add(Place.builder().id(placeId2).build());
    long placeId3 = 3L;
    findResultWith5Items.add(Place.builder().id(placeId3).build());
    long placeId4 = 4L;
    findResultWith5Items.add(Place.builder().id(placeId4).build());
    long placeId5 = 5L;
    findResultWith5Items.add(Place.builder().id(placeId5).build());

    int size = 2;
    List<Place> findResultWith2Items = new ArrayList<>();
    long placeId6 = 6L;
    findResultWith2Items.add(Place.builder().id(placeId6).build());
    long placeId7 = 7L;
    findResultWith2Items.add(Place.builder().id(placeId7).build());

    //Mock 행동 정의 - placeRepository
    willReturn(findResultWith5Items).given(placeRepository).findPlaceOrderByOpinionSizeDesc(5);
    willReturn(findResultWith2Items).given(placeRepository).findPlaceOrderByOpinionSizeDesc(size);

    //WHEN
    List<PlaceWithOpinionSizeResponse> nullResult = placeService.inquiryMostOpinedPlace(null);
    List<PlaceWithOpinionSizeResponse> itemsResult = placeService.inquiryMostOpinedPlace(size);

    //THEN
    assertAll(
        //성공 케이스 - 1 - size 가 null인 경우
        () -> assertSame(5, nullResult.size()),
        () -> assertSame(findResultWith5Items.get(0).getId(), nullResult.get(0).getId()),
        () -> assertSame(findResultWith5Items.get(1).getId(), nullResult.get(1).getId()),
        () -> assertSame(findResultWith5Items.get(2).getId(), nullResult.get(2).getId()),
        () -> assertSame(findResultWith5Items.get(3).getId(), nullResult.get(3).getId()),
        () -> assertSame(findResultWith5Items.get(4).getId(), nullResult.get(4).getId()),
        //성공 케이스 - 2 - size 가 2인 경우
        () -> assertSame(size, itemsResult.size()),
        () -> assertSame(findResultWith2Items.get(0).getId(), itemsResult.get(0).getId()),
        () -> assertSame(findResultWith2Items.get(1).getId(), itemsResult.get(1).getId())
    );
  }
}