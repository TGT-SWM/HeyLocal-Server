package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.ActivityTasteType;
import com.heylocal.traveler.domain.travelon.PlaceTasteType;
import com.heylocal.traveler.domain.travelon.SnsTasteType;
import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.dto.TravelTypeGroupDto;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.heylocal.traveler.dto.TravelOnDto.TravelOnRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

class TravelOnServiceTest {
  @Mock
  private TravelOnRepository travelOnRepository;
  @Mock
  private RegionRepository regionRepository;
  @Mock
  private UserRepository userRepository;
  private TravelOnService travelOnService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    travelOnService = new TravelOnService(travelOnRepository, regionRepository, userRepository);
  }

  @Test
  @DisplayName("새로운 여행On 등록 성공 케이스")
  void addNewTravelOnSucceedTest() {
    //GIVEN
    long loginUserId = 3;
    TravelOnRequest request = getTravelOnRequest();
    LoginUser loginUser = LoginUser.builder()
        .id(loginUserId)
        .build();
    User author = User.builder()
        .accountId("testAccountId")
        .password("testPassword")
        .nickname("testNickname")
        .userRole(UserRole.TRAVELER)
        .id(loginUserId)
        .build();
    String state = "경기도";
    String city = "성남시";
    Region region = Region.builder()
        .state(state)
        .city(city)
        .build();

    //Mock 행동 정의 - UserRepository
    willReturn(Optional.of(author)).given(userRepository).findById(loginUserId);

    //Mock 행동 정의 - RegionRepository
    willReturn(Optional.of(region)).given(regionRepository).findByStateAndCity(eq(state), eq(city));

    //Mock 행동 정의 - TravelOnRepository
    willDoNothing().given(travelOnRepository).saveTravelOn(any());

    //WHEN


    //THEN
    //성공 케이스 - 1 - 예외 없이 여행On 등록
    assertDoesNotThrow(() -> travelOnService.addNewTravelOn(request, loginUser));
  }

  @Test
  @DisplayName("새로운 여행On 등록 실패 케이스 - 잘못된 Region")
  void addNewTravelOnWrongRegionTest() {
    //GIVEN
    long loginUserId = 3;
    TravelOnRequest request = getTravelOnRequest();
    LoginUser loginUser = LoginUser.builder()
        .id(loginUserId)
        .build();
    User author = User.builder()
        .accountId("testAccountId")
        .password("testPassword")
        .nickname("testNickname")
        .userRole(UserRole.TRAVELER)
        .id(loginUserId)
        .build();
    String state = "경기도";
    String city = "성남시";
    Region region = Region.builder()
        .state(state)
        .city(city)
        .build();

    //Mock 행동 정의 - UserRepository
    willReturn(Optional.of(author)).given(userRepository).findById(loginUserId);

    //Mock 행동 정의 - RegionRepository
    willReturn(Optional.empty()).given(regionRepository).findByStateAndCity(eq(state), eq(city));

    //Mock 행동 정의 - TravelOnRepository
    willDoNothing().given(travelOnRepository).saveTravelOn(any());

    //WHEN


    //THEN
    //실패 케이스 - 1 - 잘못된 Region 인 경우
    assertThrows(
        BadArgumentException.class,
        () -> travelOnService.addNewTravelOn(request, loginUser)
    );
  }

  //TODO - 모든 여행 On 목록 조회

  private TravelOnRequest getTravelOnRequest() {
    TravelOnRequest request;
    String title = "testTitle";
    RegionDto.RegionRequest region = RegionDto.RegionRequest.builder()
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
    TravelTypeGroupDto.TravelTypeGroupRequest travelTypeGroupRequest = TravelTypeGroupDto.TravelTypeGroupRequest.builder()
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