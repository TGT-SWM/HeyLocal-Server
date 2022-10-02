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
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.mapper.PlaceMapper;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.dto.OpinionDto.NewOpinionRequestRequest;
import static com.heylocal.traveler.dto.PlaceDto.PlaceRequest;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.ObjectNameProperty;
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
  @Mock
  private S3ObjectNameFormatter s3ObjectNameFormatter;
  @Mock
  private S3PresignUrlProvider s3PresignUrlProvider;
  @Mock
  private S3UrlUserContext s3UserUrlContext;
  private OpinionService opinionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    opinionService = new OpinionService(regionService, userRepository, travelOnRepository, placeRepository, opinionRepository, s3ObjectNameFormatter, s3PresignUrlProvider, s3UserUrlContext);
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
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = getOpinionRequest(placeRequest);
    TravelOn travelOn = getTravelOn(travelOnId, region);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();
    User author = User.builder().id(userId).accountId("myAccountId").nickname("myNickname").password("myPassword123!").userRole(UserRole.TRAVELER).build();
    Place existPlace = PlaceMapper.INSTANCE.toEntity(placeRequest, region);

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
            () -> assertDoesNotThrow(() -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser)),
            () -> then(placeRepository).should(times(1)).save(any()),
            () -> then(opinionRepository).should(times(1)).save(any())
        ),
        //성공 케이스 - 2 - 기존에 저장되었던 place 인 경우
        () -> assertAll(
            () -> assertDoesNotThrow(() -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser)),
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
    NewOpinionRequestRequest newOpinionRequest = getOpinionRequest(placeRequest);
    long userId = 3L;
    LoginUser loginUser = LoginUser.builder().id(userId).build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.empty()).given(travelOnRepository).findById(travelOnId);

    //WHEN

    //THEN
    //실패 케이스 - 1 - 존재하지 않는 여행On ID 인 경우
    assertThrows(NotFoundException.class, () -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser));
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
    NewOpinionRequestRequest newOpinionRequest = getOpinionRequest(placeRequest);
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
    assertThrows(NotFoundException.class, () -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser));
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
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = getOpinionRequest(placeRequest);
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
    assertThrows(BadRequestException.class, () -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser));
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
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = getOpinionRequest(placeRequest);
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
    assertThrows(ForbiddenException.class, () -> opinionService.addNewOpinion(travelOnId, newOpinionRequest, loginUser));
  }

  @Test
  @DisplayName("답변 조회")
  void inquiryOpinionsTest() throws NotFoundException {
    //GIVEN
    User author = User.builder().id(1L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    UserProfile profile = UserProfile.builder().id(1L).knowHow(0).build();
    profile.setUser(author);
    Region regionA = getRegionA();
    long travelOnId = 1L;
    TravelOn travelOn = getTravelOn(travelOnId, regionA);
    Place place = Place.builder().id(1L).region(regionA).lat(10d).lng(10d).build();
    String opinionDescription = "my opinion description";
    long opinionId = 1L;
    Opinion opinion = Opinion.builder().id(opinionId).place(place).description(opinionDescription).author(author).build();
    travelOn.addOpinion(opinion);
    /* OpinionImageContent Setting */
    OpinionImageContent imgEntity1 = setNewOpinionImageContent(travelOnId, 0, 1L, ImageContentType.GENERAL, opinion);
    OpinionImageContent imgEntity2 = setNewOpinionImageContent(travelOnId, 1,2L, ImageContentType.GENERAL, opinion);
    OpinionImageContent imgEntity3 = setNewOpinionImageContent(travelOnId, 2,4L, ImageContentType.RECOMMEND_FOOD, opinion);

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(travelOn)).given(travelOnRepository).findById(travelOnId);
    willReturn(Optional.empty()).given(travelOnRepository).findById(not(eq(travelOnId)));

    //Mock 행동 정의 - s3ObjectNameFormatter
    Map<ObjectNameProperty, String> map1 = new ConcurrentHashMap<>();
    map1.put(ObjectNameProperty.OBJECT_INDEX, String.valueOf(0));
    Map<ObjectNameProperty, String> map2 = new ConcurrentHashMap<>();
    map2.put(ObjectNameProperty.OBJECT_INDEX, String.valueOf(1));
    Map<ObjectNameProperty, String> map3 = new ConcurrentHashMap<>();
    map3.put(ObjectNameProperty.OBJECT_INDEX, String.valueOf(2));
    willReturn(map1).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(eq(imgEntity1.getObjectKeyName()));
    willReturn(map2).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(eq(imgEntity2.getObjectKeyName()));
    willReturn(map3).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(eq(imgEntity3.getObjectKeyName()));

    //WHEN
    List<OpinionDto.OpinionWithPlaceResponse> resultList = opinionService.inquiryOpinions(travelOnId);
    OpinionDto.OpinionWithPlaceResponse result = resultList.get(0);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회결과
        () -> assertEquals(opinionDescription, result.getDescription()),
        //성공 케이스 - 2 - Image 엔티티 조회 결과
        () -> assertSame(2, result.getGeneralImgDownloadImgUrl().size()),
        () -> assertSame(1, result.getFoodImgDownloadImgUrl().size()),
        () -> assertSame(0, result.getDrinkAndDessertImgDownloadImgUrl().size()),
        () -> assertSame(0, result.getPhotoSpotImgDownloadImgUrl().size()),
        //실패 케이스 - 1 - 존재하지 않는 여행On ID 인 경우
        () -> assertThrows(NotFoundException.class, () -> opinionService.inquiryOpinions(travelOnId + 1))
    );
  }

  private OpinionImageContent setNewOpinionImageContent(long travelOnId, int objectIndex, long imageId, ImageContentType type, Opinion opinion) {
    OpinionImageContent imageEntity = OpinionImageContent.builder()
        .id(imageId)
        .imageContentType(type)
        .objectKeyName("opinions/" + travelOnId + "/" + opinion.getId() + "/" + type.name() + "/" + objectIndex++)
        .build();

    imageEntity.setOpinion(opinion);

    return imageEntity;
  }

  @Test
  @DisplayName("답변 수정 - 성공 케이스")
  void updateOpinionSucceedTest() throws BadRequestException, ForbiddenException, NotFoundException {
    //GIVEN
    Region regionOfTravelOn = getRegionA();
    long savedTravelOnId = 2L;
    TravelOn savedTravelOn = getTravelOn(savedTravelOnId, regionOfTravelOn);
    User opinionAuthor = User.builder().id(3L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String addressOfPlace = placeRequest.getAddress();
    Place placeOfSavedOpinion = Place.builder()
        .id(placeId)
        .region(regionOfTravelOn)
        .address(addressOfPlace)
        .build();
    long savedOpinionId = 1L;
    Opinion savedOpinion = Opinion.builder()
        .id(savedOpinionId)
        .travelOn(savedTravelOn)
        .author(opinionAuthor)
        .region(regionOfTravelOn)
        .place(placeOfSavedOpinion)
        .build();
    String updateDescriptionOfOpinion = "changed description";
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = OpinionDto.NewOpinionRequestRequest.builder()
        .place(placeRequest)
        .description(updateDescriptionOfOpinion)
//        .generalImgContentUrlList(new ArrayList<>())
//        .drinkAndDessertImgContentUrlList(new ArrayList<>())
//        .foodImgContentUrlList(new ArrayList<>())
//        .photoSpotImgContentUrlList(new ArrayList<>())
        .build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(savedTravelOn)).given(travelOnRepository).findById(savedTravelOnId);

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(savedOpinion)).given(opinionRepository).findByIdAndTravelOn(savedOpinionId, savedTravelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.of(regionOfTravelOn)).given(regionService).getRegionByAddress(eq(addressOfPlace));

    //Mock 행동 정의 - placeRepository
    willReturn(Optional.of(placeOfSavedOpinion)).given(placeRepository).findById(placeId);

    //WHEN
    opinionService.updateOpinion(savedTravelOnId, savedOpinionId, newOpinionRequest);

    //THEN
    assertEquals(updateDescriptionOfOpinion, savedOpinion.getDescription());
  }

  @Test
  @DisplayName("답변 수정 - 존재하지 않는 여행On ID")
  void updateOpinionNotExistTravelOnTest() {
    //GIVEN
    long savedTravelOnId = 2L;
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    long savedOpinionId = 1L;
    String updateDescriptionOfOpinion = "changed description";
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = NewOpinionRequestRequest.builder()
        .place(placeRequest)
        .description(updateDescriptionOfOpinion)
        .build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.empty()).given(travelOnRepository).findById(anyLong());

    //WHEN

    //THEN
    assertThrows(
        NotFoundException.class,
        () -> opinionService.updateOpinion(savedTravelOnId, savedOpinionId, newOpinionRequest)
    );
  }

  @Test
  @DisplayName("답변 수정 - 존재하지 않는 답변")
  void updateOpinionNotExistOpinionTest() {
    //GIVEN
    Region regionOfTravelOn = getRegionA();
    long savedTravelOnId = 2L;
    TravelOn savedTravelOn = getTravelOn(savedTravelOnId, regionOfTravelOn);
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    long savedOpinionId = 1L;
    String updateDescriptionOfOpinion = "changed description";
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = NewOpinionRequestRequest.builder()
        .place(placeRequest)
        .description(updateDescriptionOfOpinion)
        .build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(savedTravelOn)).given(travelOnRepository).findById(savedTravelOnId);

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.empty()).given(opinionRepository).findByIdAndTravelOn(anyLong(), anyLong());

    //WHEN

    //THEN
    assertThrows(
        NotFoundException.class,
        () -> opinionService.updateOpinion(savedTravelOnId, savedOpinionId, newOpinionRequest)
    );
  }

  @Test
  @DisplayName("답변 수정 - 업데이트할 답변의 장소 주소가 올바르지 않은 경우")
  void updateOpinionWrongNewPlaceAddressTest() throws BadRequestException {
    //GIVEN
    Region regionOfTravelOn = getRegionA();
    long savedTravelOnId = 2L;
    TravelOn savedTravelOn = getTravelOn(savedTravelOnId, regionOfTravelOn);
    User opinionAuthor = User.builder().id(3L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String addressOfPlace = placeRequest.getAddress();
    Place placeOfSavedOpinion = Place.builder()
        .id(placeId)
        .region(regionOfTravelOn)
        .address(addressOfPlace)
        .build();
    long savedOpinionId = 1L;

    Opinion savedOpinion = Opinion.builder()
        .id(savedOpinionId)
        .travelOn(savedTravelOn)
        .author(opinionAuthor)
        .region(regionOfTravelOn)
        .place(placeOfSavedOpinion)
        .build();
    String updateDescriptionOfOpinion = "changed description";
    NewOpinionRequestRequest newOpinionRequest = OpinionDto.NewOpinionRequestRequest.builder()
        .place(placeRequest)
        .description(updateDescriptionOfOpinion)
        .build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(savedTravelOn)).given(travelOnRepository).findById(savedTravelOnId);

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(savedOpinion)).given(opinionRepository).findByIdAndTravelOn(savedOpinionId, savedTravelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.of(placeOfSavedOpinion)).given(placeRepository).findById(placeId);

    //WHEN

    //THEN
    assertThrows(
        NotFoundException.class,
        () -> opinionService.updateOpinion(savedTravelOnId, savedOpinionId, newOpinionRequest)
    );
  }

  @Test
  @DisplayName("답변 수정 - 여행On의 지역과 업데이트할 답변의 장소 지역이 다른 경우")
  void updateOpinionDifferentPlaceRegionTest() throws BadRequestException, ForbiddenException, NotFoundException {
    //GIVEN
    Region regionOfTravelOn = getRegionA();
    Region differentRegion = getRegionB();
    long savedTravelOnId = 2L;
    TravelOn savedTravelOn = getTravelOn(savedTravelOnId, regionOfTravelOn);
    User opinionAuthor = User.builder().id(3L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String addressOfPlace = placeRequest.getAddress();
    Place placeOfSavedOpinion = Place.builder()
        .id(placeId)
        .region(regionOfTravelOn)
        .address(addressOfPlace)
        .build();
    long savedOpinionId = 1L;
    Opinion savedOpinion = Opinion.builder()
        .id(savedOpinionId)
        .travelOn(savedTravelOn)
        .author(opinionAuthor)
        .region(regionOfTravelOn)
        .place(placeOfSavedOpinion)
        .build();
    String diffRegionAddress = "different region address";
    PlaceRequest diffRegionPlaceRequest = getPlaceRequest(placeId + 1, diffRegionAddress);
    String updateDescriptionOfOpinion = "changed description";
    OpinionDto.NewOpinionRequestRequest newOpinionRequest = OpinionDto.NewOpinionRequestRequest.builder()
        .place(diffRegionPlaceRequest)
        .description(updateDescriptionOfOpinion)
        .build();

    //Mock 행동 정의 - travelOnRepository
    willReturn(Optional.of(savedTravelOn)).given(travelOnRepository).findById(savedTravelOnId);

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(savedOpinion)).given(opinionRepository).findByIdAndTravelOn(savedOpinionId, savedTravelOnId);

    //Mock 행동 정의 - regionService
    willReturn(Optional.of(regionOfTravelOn)).given(regionService).getRegionByAddress(eq(addressOfPlace));
    willReturn(Optional.of(differentRegion)).given(regionService).getRegionByAddress(eq(diffRegionAddress));

    //Mock 행동 정의 - placeRepository
    willReturn(Optional.of(placeOfSavedOpinion)).given(placeRepository).findById(placeId);

    //WHEN

    //THEN
    assertThrows(
        ForbiddenException.class,
        () -> opinionService.updateOpinion(savedTravelOnId, savedOpinionId, newOpinionRequest)
    );
  }

  @Test
  @DisplayName("답변 작성자 확인")
  void isAuthorTest() throws NotFoundException {
    //GIVEN
    long authorId = 1L;
    long notAuthorId = authorId + 5L;
    User author = User.builder().id(authorId).build();
    long opinionId = 2L;
    Opinion opinion = Opinion.builder().id(opinionId).author(author).build();

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(opinion)).given(opinionRepository).findById(opinionId);
    willReturn(Optional.empty()).given(opinionRepository).findById(not(eq(opinionId)));

    //WHEN
    boolean isAuthorResult = opinionService.isAuthor(authorId, opinionId);
    boolean isNotAuthorResult = opinionService.isAuthor(notAuthorId, opinionId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 답변 작성자가 맞을 경우
        () -> assertTrue(isAuthorResult),
        //실패 케이스 - 1 - 답변 작성자가 아닌 경우
        () -> assertFalse(isNotAuthorResult)
    );
  }

  @Test
  @DisplayName("답변 삭제")
  void removeOpinionTest() {
    //GIVEN
    Region regionOfTravelOn = getRegionA();
    long savedTravelOnId = 2L;
    TravelOn savedTravelOn = getTravelOn(savedTravelOnId, regionOfTravelOn);
    User opinionAuthor = User.builder().id(3L).accountId("myAccountId").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    long placeId = 4L;
    PlaceRequest placeRequest = getPlaceRequest(placeId);
    String addressOfPlace = placeRequest.getAddress();
    Place placeOfSavedOpinion = Place.builder()
        .id(placeId)
        .region(regionOfTravelOn)
        .address(addressOfPlace)
        .build();
    long wrongOpinionId = 1L;
    long savedOpinionId = 2L;
    Opinion opinion = Opinion.builder()
        .id(savedOpinionId)
        .travelOn(savedTravelOn)
        .author(opinionAuthor)
        .region(regionOfTravelOn)
        .place(placeOfSavedOpinion)
        .build();

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(opinion)).given(opinionRepository).findByIdAndTravelOn(savedOpinionId, savedTravelOnId);
    willReturn(Optional.empty()).given(opinionRepository).findByIdAndTravelOn(wrongOpinionId, savedTravelOnId);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> opinionService.removeOpinion(savedTravelOnId, savedOpinionId)),
        //실패 케이스 - 1
        () -> assertThrows(NotFoundException.class, () -> opinionService.removeOpinion(savedTravelOnId, wrongOpinionId))
    );
  }

  @Test
  @DisplayName("해당 장소와 연관된 답변들을 조회하는 메서드")
  void inquiryOpinionsByPlaceTest() {
    //GIVEN
    long placeId = 1L;
    PageDto.PageRequest pageRequest = PageDto.PageRequest.builder().lastItemId(null).size(2).build();

    //Mock 행동 정의 - opinionRepository
    List<Opinion> opinionList = new ArrayList<>();
    long opinion1Id = 1L;
    long opinion2Id = 2L;
    Opinion opinion1 = Opinion.builder().id(opinion1Id).build();
    Opinion opinion2 = Opinion.builder().id(opinion2Id).build();
    opinionList.add(opinion1);
    opinionList.add(opinion2);
    willReturn(opinionList).given(opinionRepository).findByPlaceId(placeId, null, 2);

    //WHEN
    List<OpinionDto.OpinionResponse> result = opinionService.inquiryOpinionsByPlace(placeId, pageRequest);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 정상 응답
        () -> assertSame(2, result.size()),
        () -> assertSame(opinion1Id, result.get(0).getId()),
        () -> assertSame(opinion2Id, result.get(1).getId())
    );
  }

  private NewOpinionRequestRequest getOpinionRequest(PlaceRequest place) {
    return OpinionDto.NewOpinionRequestRequest.builder()
        .description("myDescription")
        .place(place)
        .facilityCleanliness(EvaluationDegree.GOOD)
        .costPerformance(EvaluationDegree.GOOD)
        .waiting(false)
        .coffeeType(CoffeeType.BITTER)
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

  private PlaceRequest getPlaceRequest(long placeId, String address) {
    return PlaceRequest.builder()
        .id(placeId)
        .category(PlaceCategory.CE7)
        .name("myPlace")
        .roadAddress("myRoadAddress")
        .address(address)
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
    author.setUserProfile(profile);

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