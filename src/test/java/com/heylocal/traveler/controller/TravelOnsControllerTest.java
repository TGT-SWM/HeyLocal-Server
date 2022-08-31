package com.heylocal.traveler.controller;

import com.heylocal.traveler.domain.travelon.ActivityTasteType;
import com.heylocal.traveler.domain.travelon.PlaceTasteType;
import com.heylocal.traveler.domain.travelon.SnsTasteType;
import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.service.TravelOnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.heylocal.traveler.dto.RegionDto.RegionRequest;
import static com.heylocal.traveler.dto.TravelOnDto.TravelOnRequest;
import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.willReturn;

class TravelOnsControllerTest {
  @Mock
  private TravelOnService travelOnService;
  @Mock
  private BindingResult bindingResult;
  private TravelOnsController travelOnsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    travelOnsController = new TravelOnsController(travelOnService);
  }

  @Test
  @DisplayName("여행 On 등록 성공")
  void createTravelOnTest() {
    //GIVEN
    TravelOnRequest request = getTravelOnRequest();
    LoginUser loginUser = LoginUser.builder().id(3L).build();

    //Mock 행동 정의 - BindingResult
    willReturn(false).given(bindingResult).hasFieldErrors();

    //WHEN

    //THEN
    assertDoesNotThrow(
        () -> travelOnsController.createTravelOn(request, bindingResult, loginUser)
    );
  }

  private TravelOnRequest getTravelOnRequest() {
    TravelOnRequest request;
    String title = "testTitle";
    RegionRequest region = RegionRequest.builder()
        .city("성남시")
        .state("경기도")
        .build();
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    Set<MemberType> memberTypeSet = new HashSet<>();
    memberTypeSet.add(MemberType.PARENT);
    memberTypeSet.add(MemberType.CHILD);
    int accommodationMaxCost = 100000;
    Set<AccommodationType> accommodationTypeSet = new HashSet<>();
    accommodationTypeSet.add(AccommodationType.HOTEL);
    accommodationTypeSet.add(AccommodationType.GUEST_HOUSE);
    int foodMaxCost = 100000;
    Set<FoodType> foodTypeSet = new HashSet<>();
    foodTypeSet.add(FoodType.JAPANESE);
    foodTypeSet.add(FoodType.KOREAN);
    int drinkMaxCost = 100000;
    Set<DrinkType> drinkTypeSet = new HashSet<>();
    drinkTypeSet.add(DrinkType.BEER);
    drinkTypeSet.add(DrinkType.SOJU);
    TravelTypeGroupRequest travelTypeGroupRequest = TravelTypeGroupRequest.builder()
        .activityTasteType(ActivityTasteType.HARD)
        .placeTasteType(PlaceTasteType.FAMOUS)
        .snsTasteType(SnsTasteType.YES)
        .build();
    request = TravelOnRequest.builder()
        .title(title)
        .region(region)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .memberTypeSet(memberTypeSet)
        .accommodationMaxCost(accommodationMaxCost)
        .accommodationTypeSet(accommodationTypeSet)
        .foodMaxCost(foodMaxCost)
        .foodTypeSet(foodTypeSet)
        .drinkMaxCost(drinkMaxCost)
        .drinkTypeSet(drinkTypeSet)
        .travelTypeGroup(travelTypeGroupRequest)
        .build();
    return request;
  }
}