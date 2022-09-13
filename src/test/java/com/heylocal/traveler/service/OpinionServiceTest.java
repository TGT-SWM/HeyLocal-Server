package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.*;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.travelon.opinion.CoffeeType;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.heylocal.traveler.dto.OpinionDto.*;
import static com.heylocal.traveler.dto.OpinionDto.OpinionRequest;
import static com.heylocal.traveler.dto.PlaceDto.PlaceRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

class OpinionServiceTest {
  @Mock
  private RegionService regionService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private TravelOnRepository travelOnRepository;
  @Mock
  private PlaceRepository placeRepository;
  @Mock
  private OpinionRepository opinionRepository;
  private OpinionService opinionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    opinionService = new OpinionService(regionService, userRepository, travelOnRepository, placeRepository, opinionRepository);
  }

  @Test
  @DisplayName("새 답변 등록 - 성공 케이스")
  void addNewOpinionSucceedTest() throws BadRequestException {
    /* 여행On id가 유효하고, 주소->Region 매핑이 되고, 여행On Region과 답변 장소의 Region이 같고, 작성자 조회가 되는 경우 */

    //GIVEN
    long travelOnId = 1L;
    long placeId = 2L;
    Region region = getRegionA();
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String placeAddress = placeRequest.getAddress();
    OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    TravelOn travelOn = getTravelOn(travelOnId, region);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();
    User author = User.builder().id(userId).accountId("myAccountId").nickname("myNickname").password("myPassword123!").userRole(UserRole.TRAVELER).build();
    Place existPlace = placeRequest.toEntity(region);

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.of(region)).given(regionService).getRegionByAddress(eq(placeAddress));

    //Mock 행동 정의 - userRepository
    willReturn(Optional.of(author)).given(userRepository).findById(userId);

    //Mock 행동 정의 - placeRepository
    willReturn(Optional.empty()).willReturn(Optional.of(existPlace)).given(placeRepository).findById(placeId);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 기존에 저장되었던 place 가 아닌 경우
        () -> assertAll(
            () -> assertDoesNotThrow(() -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser)),
            () -> then(placeRepository).should(times(1)).save(any()),
            () -> then(opinionRepository).should(times(1)).save(any())
        ),
        //성공 케이스 - 2 - 기존에 저장되었던 place 인 경우
        () -> assertAll(
            () -> assertDoesNotThrow(() -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser)),
            () -> then(placeRepository).should(times(1)).save(any()),
            () -> then(opinionRepository).should(times(2)).save(any())
        )
    );
  }

  @Test
  @DisplayName("새 답변 등록 - 존재하지 않는 여행On ID 인 경우")
  void addNewOpinionNotExistTravelOnIdTest() {
    //GIVEN
    long travelOnId = 1L;
    long placeId = 2L;
    Region region = getRegionA();
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.empty()).given(travelOnRepository).findById(travelOnId);

    //WHEN

    //THEN
    //실패 케이스 - 1 - 존재하지 않는 여행On ID 인 경우
    assertThrows(NotFoundException.class, () -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser));
  }

  @Test
  @DisplayName("새 답변 등록 - 주소가 잘못된 경우")
  void addNewOpinionWrongAddressTest() throws BadRequestException {
    //GIVEN
    long travelOnId = 1L;
    long placeId = 2L;
    Region region = getRegionA();
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String placeAddress = placeRequest.getAddress();
    OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    TravelOn travelOn = getTravelOn(travelOnId, region);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.empty()).given(regionService).getRegionByAddress(eq(placeAddress));

    //WHEN

    //THEN
    //실패 케이스 - 1 - 주소가 잘못된 경우
    assertThrows(NotFoundException.class, () -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser));
  }

  @Test
  @DisplayName("새 답변 등록 - 주소 형식이 틀린 경우")
  void addNewOpinionWrongFormatAddressTest() throws BadRequestException {
    //GIVEN
    long travelOnId = 1L;
    long placeId = 2L;
    Region region = getRegionA();
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String placeAddress = placeRequest.getAddress();
    OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    TravelOn travelOn = getTravelOn(travelOnId, region);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);

    //Mock 행동 정의 - regionService
    willThrow(BadRequestException.class).given(regionService).getRegionByAddress(eq(placeAddress));

    //WHEN

    //THEN
    //실패 케이스 - 1 - 주소 형식이 잘못된 경우
    assertThrows(BadRequestException.class, () -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser));
  }

  @Test
  @DisplayName("새 답변 등록 - 여행On의 지역과 다른 지역의 장소로 답변한 경우")
  void addNewOpinionNotSameRegionTest() throws BadRequestException {
    //GIVEN
    long travelOnId = 1L;
    long placeId = 2L;
    Region regionA = getRegionA();
    Region regionB = getRegionB();
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String placeAddress = placeRequest.getAddress();
    OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    TravelOn travelOn = getTravelOn(travelOnId, regionA);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.of(regionB)).given(regionService).getRegionByAddress(eq(placeAddress));

    //WHEN

    //THEN
    //실패 케이스 - 1 - 여행On의 지역과 다른 지역의 장소로 답변한 경우
    assertThrows(ForbiddenException.class, () -> opinionService.addNewOpinion(travelOnId, opinionRequest, loginUser));
  }

  @Test
  @DisplayName("답변 조회")
  void inquiryOpinionsTest() throws NotFoundException {
    //GIVEN
    User author = User.builder().id(1L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    UserProfile profile = UserProfile.builder().id(1L).knowHow(0).build();
    profile.associateUser(author);
    Region regionA = getRegionA();
    long travelOnId = 1L;
    TravelOn travelOn = getTravelOn(travelOnId, regionA);
    Place place = Place.builder().id(1L).region(regionA).lat(10d).lng(10d).build();
    String opinionDescription = "my opinion description";
    Opinion opinion = Opinion.builder().id(1L).place(place).description(opinionDescription).author(author).build();

    travelOn.addOpinion(opinion);

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);
    willReturn(Optional.empty()).given(travelOnRepository).findById(not(eq(travelOnId)));

    //WHEN
    List<OpinionResponse> result = opinionService.inquiryOpinions(travelOnId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회결과
        () -> assertEquals(opinionDescription, result.get(0).getDescription()),
        //실패 케이스 - 1 - 존재하지 않는 여행On ID 인 경우
        () -> assertThrows(NotFoundException.class, () -> opinionService.inquiryOpinions(travelOnId + 1))
    );
  }

  private OpinionRequest getOpinionRequest(PlaceRequest place) {
    return OpinionRequest.builder()
        .description("myDescription")
        .place(place)
        .kindness(EvaluationDegree.GOOD)
        .facilityCleanliness(EvaluationDegree.GOOD)
        .accessibility(EvaluationDegree.GOOD)
        .costPerformance(EvaluationDegree.GOOD)
        .waiting(false)
        .drink(EvaluationDegree.GOOD)
        .coffeeType(CoffeeType.BITTER)
        .recommendDrink("myDrink")
        .recommendDessert("myDessert")
        .build();
  }

  private PlaceRequest getPlaceRequest(long placeId) {
    return PlaceRequest.builder()
        .id(placeId)
        .category(PlaceCategory.CE7)
        .name("myPlace")
        .roadAddress("myRoadAddress")
        .address("myAddress")
        .lat(10)
        .lng(10)
        .kakaoLink("myLink")
        .build();
  }

  /**
   * <pre>
   * 새 TravelOn 객체를 반환하는 메서드
   * </pre>
   * @return
   */
  private TravelOn getTravelOn(long id, Region region) {
    TravelOn travelOn;
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    int accommodationMaxCost = 100000;
    int foodMaxCost = 100000;
    int drinkMaxCost = 100000;

    User author = User.builder().id(1L).accountId("accountId").password("password").nickname("nickname").userRole(UserRole.TRAVELER).build();
    UserProfile profile = UserProfile.builder().id(1L).knowHow(100).build();
    author.registerUserProfile(profile);

    travelOn = TravelOn.builder()
        .id(id)
        .title("title")
        .region(region)
        .views(1)
        .author(author)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .accommodationMaxCost(accommodationMaxCost)
        .foodMaxCost(foodMaxCost)
        .drinkMaxCost(drinkMaxCost)
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

  private Region getRegionA() {
    return Region.builder().id(1L).state("경기도").city("성남시").build();
  }

  private Region getRegionB() {
    return Region.builder().id(2L).state("서울특별시").build();
  }
}