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
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.ForbiddenException;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.exception.service.TaskRejectException;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.heylocal.traveler.dto.PageDto.PageRequest;
import static com.heylocal.traveler.dto.TravelOnDto.*;
import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class TravelOnsControllerTest {
  @Mock
  private BindingErrorMessageProvider messageProvider;
  @Mock
  private TravelOnService travelOnService;
  @Mock
  private BindingResult bindingResult;
  private TravelOnsController travelOnsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    travelOnsController = new TravelOnsController(messageProvider, travelOnService);
  }

  @Test
  @DisplayName("여행 On 등록 성공")
  void createTravelOnSucceedTest() {
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

  @Test
  @DisplayName("여행 On 등록 실패 - 비어있는 필드를 넘겼을 경우")
  void createTravelOnEmptyTitleTest() {
    //GIVEN
    TravelOnRequest request = getTravelOnRequest();
    LoginUser loginUser = LoginUser.builder().id(3L).build();

    //Mock 행동 정의 - BindingResult
    willReturn(true).given(bindingResult).hasFieldErrors();
    FieldError fieldError = new FieldError("request", "title", "title 필드가 비어 있습니다.");
    willReturn(fieldError).given(bindingResult).getFieldError();

    //WHEN

    //THEN
    assertThrows(
        BadRequestException.class,
        () -> travelOnsController.createTravelOn(request, bindingResult, loginUser)
    );
  }

  @Test
  @DisplayName("여행On 목록 조회")
  void getTravelOnsTest() {
    //GIVEN
    AllTravelOnGetRequest succeedRequest = getAllTravelOnRequest();

    //WHEN

    //THEN
    assertAll(
        //성공 케이스
        () -> assertDoesNotThrow(() -> travelOnsController.getTravelOns(succeedRequest))
    );
  }

  @Test
  @DisplayName("여행On 상세 조회")
  void getTravelOnTest() throws BadArgumentException {
    //GIVEN
    long existTravelOnId = 1L;
    long notExistTravelOnId = 3L;

    //Mock 행동 정의 - travelOnService
    willThrow(BadArgumentException.class).given(travelOnService).inquiryTravelOn(notExistTravelOnId);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 정상적인 여행On ID를 전달받았을 때
        () -> assertDoesNotThrow(() -> travelOnsController.getTravelOn(existTravelOnId)),
        //실패 케이스 - 1 - 존재하지 않는 여행On ID를 전달받았을 때
        () -> assertThrows(NotFoundException.class, () -> travelOnsController.getTravelOn(notExistTravelOnId))
    );
  }

  @Test
  @DisplayName("여행On 수정 핸들러 - 성공 케이스")
  void updateTravelOnSucceedTest() throws BadArgumentException, ForbiddenException, BadRequestException, NotFoundException {
    //GIVEN
    long travelOnId = 1L;
    long ownerId = 2L;
    LoginUser loginUser = LoginUser.builder().id(ownerId).build();

    //Mock 행동 정의 - bindingResult
    willReturn(false).given(bindingResult).hasFieldErrors();

    //Mock 행동 정의 - travelOnService
    willReturn(true).given(travelOnService).isAuthor(ownerId, travelOnId);

    //WHEN
    travelOnsController.updateTravelOn(travelOnId, null, bindingResult, loginUser);

    //THEN
    assertDoesNotThrow(() -> travelOnsController.updateTravelOn(travelOnId, null, bindingResult, loginUser));
  }

  @Test
  @DisplayName("여행On 수정 핸들러 - 수정 권한이 없는 경우")
  void updateTravelOnForbiddenTest() throws BadArgumentException, ForbiddenException, BadRequestException, NotFoundException {
    //GIVEN
    long travelOnId = 1L;
    long noOwnerId = 2L;
    LoginUser loginUser = LoginUser.builder().id(noOwnerId).build();

    //Mock 행동 정의 - bindingResult
    willReturn(false).given(bindingResult).hasFieldErrors();

    //Mock 행동 정의 - travelOnService
    willReturn(false).given(travelOnService).isAuthor(noOwnerId, travelOnId);

    //WHEN

    //THEN
    assertThrows(ForbiddenException.class, () -> travelOnsController.updateTravelOn(travelOnId, null, bindingResult, loginUser));
  }

  @Test
  @DisplayName("여행On 수정 핸들러 - 입력 형식이 틀린 경우")
  void updateTravelOnWrongInputFormTest() throws BadArgumentException, ForbiddenException, BadRequestException, NotFoundException {
    //GIVEN
    long travelOnId = 1L;
    long noOwnerId = 2L;
    LoginUser loginUser = LoginUser.builder().id(noOwnerId).build();

    //Mock 행동 정의 - bindingResult
    willReturn(true).given(bindingResult).hasFieldErrors();

    //Mock 행동 정의 - travelOnService
    willReturn(false).given(travelOnService).isAuthor(noOwnerId, travelOnId);

    //WHEN

    //THEN
    assertThrows(BadRequestException.class, () -> travelOnsController.updateTravelOn(travelOnId, null, bindingResult, loginUser));
  }

  @Test
  @DisplayName("여행On 삭제 핸들러 - 성공 케이스")
  void deleteTravelOnSucceedTest() throws BadArgumentException {
    //GIVEN
    long existTravelOnId = 1L;
    long authorId = 2L;
    LoginUser loginUser = LoginUser.builder().id(authorId).build();

    //Mock 행동 정의 - travelOnService
    willReturn(true).given(travelOnService).isAuthor(authorId, existTravelOnId);

    //WHEN

    //THEN
    //성공 케이스 - 1 - 정상 요청
    assertDoesNotThrow(() -> travelOnsController.deleteTravelOn(existTravelOnId, loginUser));
  }

  @Test
  @DisplayName("여행On 삭제 핸들러 - 삭제 권한 없는 경우")
  void deleteTravelOnForbiddenTest() throws BadArgumentException {
    //GIVEN
    long existTravelOnId = 1L;
    long noAuthorId = 2L;
    LoginUser loginUser = LoginUser.builder().id(noAuthorId).build();

    //Mock 행동 정의 - travelOnService
    willReturn(false).given(travelOnService).isAuthor(noAuthorId, existTravelOnId);

    //WHEN

    //THEN
    //실패 케이스 - 1 - 삭제 권한이 없는 경우
    assertThrows(ForbiddenException.class, () -> travelOnsController.deleteTravelOn(existTravelOnId, loginUser));
  }

  @Test
  @DisplayName("여행On 삭제 핸들러 - 잘못된 여행On ID인 경우")
  void deleteTravelOnWrongIdTest() throws BadArgumentException {
    //GIVEN
    long notExistTravelOnId = 1L;
    long noAuthorId = 2L;
    LoginUser loginUser = LoginUser.builder().id(noAuthorId).build();

    //Mock 행동 정의 - travelOnService
    willThrow(new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")).given(travelOnService).isAuthor(noAuthorId, notExistTravelOnId);

    //WHEN

    //THEN
    //실패 케이스 - 1 - 존재하지 않는 여행On ID인 경우
    assertThrows(NotFoundException.class, () -> travelOnsController.deleteTravelOn(notExistTravelOnId, loginUser));
  }

  @Test
  @DisplayName("여행On 삭제 핸들러 - 이미 답변이 달린 경우")
  void deleteTravelOnHasOpinionTest() throws BadArgumentException, TaskRejectException {
    //GIVEN
    long existTravelOnId = 1L;
    long authorId = 2L;
    LoginUser loginUser = LoginUser.builder().id(authorId).build();

    //Mock 행동 정의 - travelOnService
    willReturn(true).given(travelOnService).isAuthor(authorId, existTravelOnId);
    willThrow(new TaskRejectException(ForbiddenCode.NO_PERMISSION, "답변이 달린 여행On 은 삭제할 수 없습니다.")).given(travelOnService).removeTravelOn(existTravelOnId);

    //WHEN

    //THEN
    //실패 케이스 - 1 - 이미 답변이 달린 여행On ID인 경우
    assertThrows(ForbiddenException.class, () -> travelOnsController.deleteTravelOn(existTravelOnId, loginUser));
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
    TravelTypeGroupRequest travelTypeGroupRequest = TravelTypeGroupRequest.builder()
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
        .foodMaxCost(foodMaxCost)
        .foodTypeSet(foodTypeSet)
        .drinkMaxCost(drinkMaxCost)
        .drinkTypeSet(drinkTypeSet)
        .travelTypeGroup(travelTypeGroupRequest)
        .build();
    return request;
  }
}