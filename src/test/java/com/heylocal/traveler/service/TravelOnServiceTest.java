package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.*;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelTypeGroupDto;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static com.heylocal.traveler.dto.TravelOnDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

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
    long regionId = 1L;
    String state = "경기도";
    String city = "성남시";
    Region region = Region.builder()
        .id(regionId)
        .state(state)
        .city(city)
        .build();

    //Mock 행동 정의 - UserRepository
    willReturn(Optional.of(author)).given(userRepository).findById(loginUserId);

    //Mock 행동 정의 - RegionRepository
    willReturn(Optional.of(region)).given(regionRepository).findById(regionId);

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
        NotFoundException.class,
        () -> travelOnService.addNewTravelOn(request, loginUser)
    );
  }

  @Test
  @DisplayName("모든 여행On 조회 - 지역 관계 없이 조회")
  void inquirySimpleTravelOnsTestWithoutRegion() {
    //GIVEN
    AllTravelOnGetRequest opinionOptionIsNullRequest = getAllTravelOnRequest();
    AllTravelOnGetRequest withOpinionOptionRequest = getAllTravelOnRequest();
    AllTravelOnGetRequest noOpinionOptionRequest = getAllTravelOnRequest();

    opinionOptionIsNullRequest.setWithOpinions(null);
    opinionOptionIsNullRequest.setRegionId(null);
    withOpinionOptionRequest.setWithOpinions(true);
    withOpinionOptionRequest.setRegionId(null);
    noOpinionOptionRequest.setWithOpinions(false);
    noOpinionOptionRequest.setRegionId(null);

    //WHEN

    //THEN
    assertAll(
        //답변 여부 무관
        () -> {
          travelOnService.inquirySimpleTravelOns(opinionOptionIsNullRequest);
          then(travelOnRepository).should(times(1)).findAll(any(), anyInt(), any(TravelOnSortType.class));
        },
        //답변 있는 것만
        () -> {
          travelOnService.inquirySimpleTravelOns(withOpinionOptionRequest);
          then(travelOnRepository).should(times(1)).findHasOpinion(any(), anyInt(), any(TravelOnSortType.class));
        },
        //답변 없는 것만
        () -> {
          travelOnService.inquirySimpleTravelOns(noOpinionOptionRequest);
          then(travelOnRepository).should(times(1)).findNoOpinion(any(), anyInt(), any(TravelOnSortType.class));
        }
    );
  }

  @Test
  @DisplayName("모든 여행On 조회 - Region을 기준으로 조회")
  void inquirySimpleTravelOnsTestWithStateCity() {
    //GIVEN
    AllTravelOnGetRequest wrongRegionRequest = getAllTravelOnRequest();
    AllTravelOnGetRequest opinionOptionIsNullRequest = getAllTravelOnRequest();
    AllTravelOnGetRequest withOpinionOptionRequest = getAllTravelOnRequest();
    AllTravelOnGetRequest noOpinionOptionRequest = getAllTravelOnRequest();

    long wrongRegionId = -1L;
    long existRegionId = 1L;
    wrongRegionRequest.setRegionId(wrongRegionId);
    opinionOptionIsNullRequest.setWithOpinions(null);
    opinionOptionIsNullRequest.setRegionId(existRegionId);
    withOpinionOptionRequest.setWithOpinions(true);
    withOpinionOptionRequest.setRegionId(existRegionId);
    noOpinionOptionRequest.setWithOpinions(false);
    noOpinionOptionRequest.setRegionId(existRegionId);

    //Mock 행동 정의 - RegionRepository
    Region region = Region.builder()
        .id(existRegionId)
        .state("myState")
        .city("myCity")
        .build();
    willReturn(Optional.of(region)).given(regionRepository).findById(existRegionId);

    //WHEN

    //THEN
    assertAll(
        //답변 여부 무관
        () -> {
          travelOnService.inquirySimpleTravelOns(opinionOptionIsNullRequest);
          then(travelOnRepository).should(times(1)).findAllByRegion(any(Region.class), any(), anyInt(), any(TravelOnSortType.class));
        },
        //답변 있는 것만
        () -> {
          travelOnService.inquirySimpleTravelOns(withOpinionOptionRequest);
          then(travelOnRepository).should(times(1)).findHasOpinionByRegion(any(Region.class), any(), anyInt(), any(TravelOnSortType.class));
        },
        //답변 없는 것만
        () -> {
          travelOnService.inquirySimpleTravelOns(noOpinionOptionRequest);
          then(travelOnRepository).should(times(1)).findNoOpinionByRegion(any(Region.class), any(), anyInt(), any(TravelOnSortType.class));
        },
        //없는 Region 인 경우
        () -> assertThrows(NotFoundException.class,
            () -> travelOnService.inquirySimpleTravelOns(wrongRegionRequest))
    );
  }

  @Test
  @DisplayName("모든 여행 On 조회 - 사용자 ID를 기준으로 조회")
  void inquirySimpleTravelOnsTestWithUserId() {
    // GIVEN (Request)
    long userId = 1L;
    long noTravelOnUserId = userId + 1;
    PageRequest pageRequest = new PageRequest(null, 10);

    // GIVEN (TravelOn)
    long travelOnCnt = 10;
    List<TravelOn> travelOns = new ArrayList<>();
    for (int i = 0; i < travelOnCnt; i++)
      travelOns.add(new TravelOn());

    given(
            travelOnRepository.findAllByUserId(
                    userId,
                    pageRequest.getLastItemId(),
                    pageRequest.getSize(),
                    TravelOnSortType.DATE)
    ).willReturn(travelOns);

    given(
            travelOnRepository.findAllByUserId(
                    noTravelOnUserId,
                    pageRequest.getLastItemId(),
                    pageRequest.getSize(),
                    TravelOnSortType.DATE)
    ).willReturn(new ArrayList<TravelOn>());

    // WHEN
    List<TravelOnSimpleResponse> successResp = travelOnService.inquirySimpleTravelOns(userId, pageRequest);
    List<TravelOnSimpleResponse> failResp = travelOnService.inquirySimpleTravelOns(noTravelOnUserId, pageRequest);

    // THEN
    assertAll(
            // 성공 케이스 - 1 - 여행 On 조회 성공
            () -> Assertions.assertThat(successResp.size()).isEqualTo(travelOnCnt),
            // 실패 케이스 - 1 - 작성한 여행 On이 없는 경우
            () -> Assertions.assertThat(failResp).isEmpty()
    );
  }

  @Test
  @DisplayName("여행 On 상세 조회")
  void inquiryTravelOnTest() {
    //GIVEN
    long travelOnId = 1L;
    long notExistTravelOnId = travelOnId + 10;
    String travelOnTitle = "title1";

    //Mock 행동 정의 - TravelOnRepository
    TravelOn travelOn = getTravelOn(travelOnId, travelOnTitle);
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);
    willReturn(Optional.empty()).given(travelOnRepository).findById(notExistTravelOnId);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 여행 On 조회
        () -> assertDoesNotThrow(() -> travelOnService.inquiryTravelOn(travelOnId)),
        //실패 케이스 - 1 - 존재하지 않는 여행 On 조회
        () -> assertThrows(NotFoundException.class, () -> travelOnService.inquiryTravelOn(notExistTravelOnId))
    );
  }

  @Test
  @DisplayName("여행On 수정")
  void updateTravelOnTest() {
    //GIVEN
    TravelOnRequest request = getTravelOnRequest();
    long existTravelOnId = 1L;
    long notExistTravelOnId = 2L;
    TravelOn travelOn = getTravelOn(existTravelOnId, "title1");

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(existTravelOnId);
    willReturn(Optional.empty()).given(travelOnRepository).findById(notExistTravelOnId);

    //WHEN


    //THEN
    assertAll(
        //성공 케이스 - 1 - 정상 요청
        () -> assertDoesNotThrow(() -> travelOnService.updateTravelOn(request, existTravelOnId)),
        //실패 케이스 - 1 - 존재하지 않는 여행On ID
        () -> assertThrows(NotFoundException.class, () -> travelOnService.updateTravelOn(request, notExistTravelOnId))
    );
  }

  @Test
  @DisplayName("여행On 삭제 - 성공 케이스")
  void removeTravelOnSucceedTest() {
    //GIVEN
    long existTravelOnId = 1L;

    //Mock 행동 정의 - travelOnRepository
    TravelOn travelOn = getTravelOn(existTravelOnId, "title1");
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(existTravelOnId);

    //WHEN

    //THEN
    assertDoesNotThrow(() -> travelOnService.removeTravelOn(existTravelOnId));
  }

  @Test
  @DisplayName("여행On 삭제 - 존재하지 않는 여행On ID 인 경우")
  void removeTravelOnNotExistIdTest() {
    //GIVEN
    long notExistTravelOnId = 1L;

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.empty()).given(travelOnRepository).findById(notExistTravelOnId);

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> travelOnService.removeTravelOn(notExistTravelOnId));
  }

  @Test
  @DisplayName("여행On 삭제 - 이미 답변이 달린 경우")
  void removeTravelOnHasOpinionTest() {
    //GIVEN
    long existTravelOnId = 1L;

    //Mock 행동 정의 - travelOnRepository
    TravelOn travelOn = getTravelOn(existTravelOnId, "title1");
    travelOn.addOpinion(new Opinion());
    travelOn.addOpinion(new Opinion());
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(existTravelOnId);

    //WHEN

    //THEN
    assertThrows(ForbiddenException.class, () -> travelOnService.removeTravelOn(existTravelOnId));
  }

  /**
   * AllTravelOnRequest 객체를 생성하는 메서드
   * @return
   */
  private AllTravelOnGetRequest getAllTravelOnRequest() {
    PageRequest pageRequest = PageRequest.builder()
        .lastItemId(0L)
        .size(10)
        .build();
    AllTravelOnGetRequest request = AllTravelOnGetRequest.builder()
        .pageRequest(pageRequest)
        .regionId(1L)
        .sortBy(TravelOnSortType.DATE)
        .withOpinions(null)
        .build();
    return request;
  }

  /**
   * <pre>
   * TravelOnRequest 객체를 생성하는 메서드
   * </pre>
   * @return
   */
  private TravelOnRequest getTravelOnRequest() {
    TravelOnRequest request;
    String title = "testTitle";
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
        .regionId(1L)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .memberTypeSet(memberTypeSet)
        .accommodationMaxCost(accommodationMaxCost)
        .accommodationTypeSet(accommodationTypeSet)
        .foodTypeSet(foodTypeSet)
        .drinkTypeSet(drinkTypeSet)
        .travelTypeGroup(travelTypeGroupRequest)
        .build();
    return request;
  }

  /**
   * <pre>
   * 새 TravelOn 객체를 반환하는 메서드
   * </pre>
   * @return
   */
  private TravelOn getTravelOn(long id, String title) {
    TravelOn travelOn;
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    int accommodationMaxCost = 100000;
    int foodMaxCost = 100000;
    int drinkMaxCost = 100000;

    Region region = Region.builder().id(1L).state("경기도").city("성남시").build();
    User author = User.builder().id(1L).accountId("accountId").password("password").nickname("nickname").userRole(UserRole.TRAVELER).build();
    UserProfile profile = UserProfile.builder().id(1L).knowHow(100).build();
    author.setUserProfile(profile);

    travelOn = TravelOn.builder()
        .id(id)
        .title(title)
        .region(region)
        .views(1)
        .author(author)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .accommodationMaxCost(accommodationMaxCost)
        .build();

    TravelMember travelMember1 = TravelMember.builder()
        .id(1L)
        .travelOn(travelOn)
        .memberType(MemberType.CHILD)
        .build();
    TravelMember travelMember2 = TravelMember.builder()
        .id(2L)
        .travelOn(travelOn)
        .memberType(MemberType.PARENT)
        .build();
    travelOn.addTravelMember(travelMember1);
    travelOn.addTravelMember(travelMember2);

    HopeAccommodation accommodation1 = HopeAccommodation.builder()
        .id(1L)
        .travelOn(travelOn)
        .type(AccommodationType.HOTEL)
        .build();
    HopeAccommodation accommodation2 = HopeAccommodation.builder()
        .id(2L)
        .travelOn(travelOn)
        .type(AccommodationType.GUEST_HOUSE)
        .build();
    travelOn.addHopeAccommodation(accommodation1);
    travelOn.addHopeAccommodation(accommodation2);

    HopeFood food1 = HopeFood.builder()
        .id(1L)
        .travelOn(travelOn)
        .type(FoodType.GLOBAL)
        .build();
    HopeFood food2 = HopeFood.builder()
        .id(2L)
        .travelOn(travelOn)
        .type(FoodType.KOREAN)
        .build();
    travelOn.addHopeFood(food1);
    travelOn.addHopeFood(food2);

    HopeDrink hopeDrink1 = HopeDrink.builder()
        .id(1L)
        .travelOn(travelOn)
        .type(DrinkType.BEER)
        .build();
    HopeDrink hopeDrink2 = HopeDrink.builder()
        .id(2L)
        .travelOn(travelOn)
        .type(DrinkType.BEER)
        .build();
    travelOn.addHopeDrink(hopeDrink1);
    travelOn.addHopeDrink(hopeDrink2);

    TravelTypeGroup travelTypeGroup = TravelTypeGroup.builder()
        .id(1L)
        .activityTasteType(ActivityTasteType.HARD)
        .placeTasteType(PlaceTasteType.FAMOUS)
        .snsTasteType(SnsTasteType.YES)
        .travelOn(travelOn)
        .build();
    travelOn.registerTravelTypeGroup(travelTypeGroup);

    return travelOn;
  }
}