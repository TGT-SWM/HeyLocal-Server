package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.PlaceItemDto.PlaceItemRequest;
import com.heylocal.traveler.dto.PlaceItemDto;
import com.heylocal.traveler.dto.PlanDto.PlanListResponse;
import com.heylocal.traveler.dto.PlanDto.PlanSchedulesRequest;
import com.heylocal.traveler.dto.PlanDto.PlanUpdateRequest;
import com.heylocal.traveler.dto.PlanDto.ScheduleRequest;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.PlanRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class PlanServiceTest {
	@Mock
	private PlanRepository planRepository;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private TravelOnRepository travelOnRepository;

	@Mock
	private RegionService regionService;

	@Spy
	private Clock clock;

	@InjectMocks
	private PlanService planService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("작성한 플랜 정보 조회")
	void getPlansTest() {
		// GIVEN - 작성자
		long userId = 1L;
		User user = User.builder()
				.id(userId)
				.build();

		// GIVEN - 지역
		Region region = Region.builder()
				.id(1L)
				.state("STATE")
				.city("CITY")
				.build();

		// GIVEN - 오늘 날짜를 Stub (UTC 2022-08-15)
		final Clock TODAY = Clock.fixed(Instant.parse("2022-08-15T00:00:00Z"), ZoneOffset.UTC);
		given(clock.instant()).willReturn(TODAY.instant());
		given(clock.getZone()).willReturn(TODAY.getZone());

		// GIVEN - 플랜 리스트
		// 지난 여행 2개, 진행 중인 여행 1개, 다가오는 여행 2개 생성
		List<Plan> plans = new ArrayList<>();
		LocalDate today = LocalDate.now(clock);
		int pastCount = 2;
		int ongoingCount = 1;
		int upcomingCount = 2;

		for (int dayAfter = -pastCount; dayAfter <= upcomingCount; ++dayAfter) {
			// 여행 날짜 (오늘 날짜에서 dayAfter 이후)
			LocalDate travelDate = LocalDate.of(
					today.getYear(),
					today.getMonth(),
					today.getDayOfMonth() + dayAfter);

			// 여행 On 객체 생성
			TravelOn travelOn = TravelOn.builder()
					.author(user)
					.region(region)
					.travelStartDate(travelDate)
					.travelEndDate(travelDate)
					.build();

			// 플랜 객체 생성
			plans.add(Plan.builder()
					.id(1L)
					.user(user)
					.travelOn(travelOn)
					.build());
		}

		// 플랜 리스트 stub
		given(planRepository.findByUserId(userId)).willReturn(plans);

		// WHEN - 성공 케이스
		PlanListResponse successCaseResponse = planService.getPlans(userId);

		// WHEN - 실패 케이스
		long noPlanUserId = userId + 1;
		PlanListResponse failCaseResponse = planService.getPlans(noPlanUserId);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 정확한 지난 여행 리스트를 반환하는지
				() -> Assertions.assertThat(successCaseResponse.getPast().size()).isEqualTo(pastCount),
				// 성공 케이스 - 2 - 정확한 진행중인 여행 리스트를 반환하는지
				() -> Assertions.assertThat(successCaseResponse.getOngoing().size()).isEqualTo(ongoingCount),
				// 성공 케이스 - 3 - 정확한 다가오는 여행 리스트를 반환하는지
				() -> Assertions.assertThat(successCaseResponse.getUpcoming().size()).isEqualTo(upcomingCount),

				// 실패 케이스 - 1 - 작성한 플랜이 없는 경우 빈 결과를 반환하는지
				() -> Assertions.assertThat(failCaseResponse.getPast()).isEmpty(),
				() -> Assertions.assertThat(failCaseResponse.getOngoing()).isEmpty(),
				() -> Assertions.assertThat(failCaseResponse.getUpcoming()).isEmpty()
		);

	}

	@Test
	@DisplayName("플랜 생성")
	void createPlanTest() {
		// GIVEN - User
		long userId = 1L;
		long anotherUserId = userId + 1;
		User user = User.builder()
				.id(userId)
				.build();

		// GIVEN - TravelOn
		long travelOnId = 1L;
		long alreadyExistsTravelId = travelOnId + 1;
		long notExistsTravelId = travelOnId + 2;

		Region region = Region.builder()
				.id(1L)
				.state("서울특별시")
				.build();

		TravelOn travelOn = TravelOn.builder()
				.id(travelOnId)
				.author(user)
				.plan(null)
				.region(region)
				.travelStartDate(LocalDate.of(2022, 10, 18))
				.travelEndDate(LocalDate.of(2022, 10, 20))
				.build();

		TravelOn planAlreadyExists = TravelOn.builder()
				.id(alreadyExistsTravelId)
				.author(user)
				.plan(new Plan())
				.region(region)
				.travelStartDate(LocalDate.of(2022, 10, 18))
				.travelEndDate(LocalDate.of(2022, 10, 20))
				.build();

		// Stub
		given(travelOnRepository.findById(travelOnId)).willReturn(Optional.of(travelOn));
		given(travelOnRepository.findById(alreadyExistsTravelId)).willReturn(Optional.of(planAlreadyExists));
		given(travelOnRepository.findById(notExistsTravelId)).willReturn(Optional.empty());


		// WHEN - THEN
		assertAll(
				// 성공 케이스 - 1 - 플랜 생성 성공
				() -> assertDoesNotThrow(() -> planService.createPlan(userId, travelOnId)),
				// 실패 케이스 - 1 - 생성 권한 없음
				() -> assertThrows(ForbiddenException.class, () -> planService.createPlan(anotherUserId, travelOnId)),
				// 실패 케이스 - 2 - 이미 존재하는 플랜 생성
				() -> assertThrows(BadRequestException.class, () -> planService.createPlan(userId, alreadyExistsTravelId)),
				// 실패 케이스 - 3 - 여행 On이 존재하지 않음
				() -> assertThrows(NotFoundException.class, () -> planService.createPlan(userId, notExistsTravelId))
		);
	}

	@Test
	@DisplayName("플랜 수정")
	void updatePlanTest() throws ForbiddenException, NotFoundException {
		// GIVEN
		long userId = 1L;
		long anotherUserId = userId + 1;
		User user = User.builder()
				.id(userId)
				.build();

		long planId = 1L;
		long anotherPlanId = planId + 1;
		Plan plan = Plan.builder()
				.id(planId)
				.title("Old Title")
				.user(user)
				.build();

		given(planRepository.findById(planId)).willReturn(Optional.of(plan)); // 플랜이 있는 경우
		given(planRepository.findById(anotherPlanId)).willReturn(Optional.empty()); // 플랜이 없는 경우

		// WHEN
		String newTitle = "New Title";
		PlanUpdateRequest request = new PlanUpdateRequest(newTitle);
		planService.updatePlan(planId, userId, request);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 플랜 수정 완료
				() -> Assertions.assertThat(plan.getTitle()).isEqualTo(newTitle),
				// 실패 케이스 - 1 - 수정하고자 하는 플랜 자체가 존재하지 않는 경우
				() -> assertThrows(NotFoundException.class, () -> planService.updatePlan(anotherPlanId, userId, request)),
				// 실패 케이스 - 2 - 수정 권한이 없는 사용자가 플랜 수정을 시도하는 경우
				() -> assertThrows(ForbiddenException.class, () -> planService.updatePlan(planId, anotherUserId, request))
		);
	}

	@Test
	@DisplayName("플랜 삭제")
	void deletePlanTest() {
		// GIVEN
		long userId = 1L;
		long anotherUserId = userId + 1;
		User user = User.builder()
				.id(userId)
				.build();

		long planId = 1L;
		long anotherPlanId = planId + 1;
		Plan plan = Plan.builder()
				.id(planId)
				.user(user)
				.travelOn(new TravelOn())
				.build();

		given(planRepository.findById(planId)).willReturn(Optional.of(plan));

		// WHEN - THEN
		assertAll(
				// 성공 케이스 - 1 - 문제 없이 플랜 삭제 완료
				() -> assertDoesNotThrow(() -> planService.deletePlan(planId, userId)),
				// 실패 케이스 - 1 - 삭제하고자 하는 플랜이 존재하지 않는 경우
				() -> assertThrows(NotFoundException.class, () -> planService.deletePlan(anotherPlanId, userId)),
				// 실패 케이스 - 2 - 삭제 권한이 없는 경우
				() -> assertThrows(ForbiddenException.class, () -> planService.deletePlan(planId, anotherUserId))
		);
	}

	@Test
	@DisplayName("플랜 내 장소 목록 조회")
	void getPlacesInPlanTest() {
		// GIVEN - Place
		List<Place> places = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			places.add(Place.builder()
					.id((long) i)
					.name("NAME")
					.address("ADDRESS")
					.roadAddress("ROADADDRESS")
					.lat(0.0)
					.lng(0.0)
					.build());
		}

		// GIVEN - PlaceItem
		List<PlaceItem> placeItems = new ArrayList<>();
		for (Place place : places) {
			placeItems.add(PlaceItem.builder()
					.place(place)
					.build());
		}

		// GIVEN - DaySchedule
		List<DaySchedule> daySchedules = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			daySchedules.add(DaySchedule.builder()
					.placeItemList(placeItems)
					.build());
		}

		// GIVEN - Plan
		long planId = 1L;
		long notFoundPlanId = planId + 1;
		Plan plan = Plan.builder()
				.id(planId)
				.dayScheduleList(daySchedules)
				.build();
		given(planRepository.findById(planId)).willReturn(Optional.of(plan));

		// WHEN - THEN
		assertAll(
				// 성공 케이스 - 1 - 예외 발생 없이 조회 성공
				() -> assertDoesNotThrow(() -> planService.getPlacesInPlan(planId)),
				// 성공 케이스 - 2 - 조회 결과가 기대와 일치
				() -> Assertions.assertThat(planService.getPlacesInPlan(planId).size()).isEqualTo(daySchedules.size()),
				// 실패 케이스 - 1 - 존재하지 않는 플랜 ID로 조회 시 예외 발생
				() -> assertThrows(NotFoundException.class, () -> planService.getPlacesInPlan(notFoundPlanId))
		);
	}

	@Test
	@DisplayName("플랜 내 장소 목록 수정")
	void updatePlacesInPlanTest() throws BadRequestException {
		// GIVEN
		long planId = 1L;
		long notExistPlanId = planId + 1;
		int days = 3;

		// GIVEN - ScheduleRequest
		List<PlaceItemRequest> places = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			places.add(PlaceItemDto.PlaceItemRequest.builder()
					.id(1L)
					.itemIndex(i)
					.category(PlaceCategory.AD5)
					.name("NAME")
					.address("ADDRESS")
					.roadAddress("ROADADDRESS")
					.lat(0.0)
					.lng(0.0)
					.build());
		}
		ScheduleRequest schedule = new ScheduleRequest(places);

		// GIVEN - PlanScheduleRequest
		List<ScheduleRequest> schedules = new ArrayList<>();
		for (int day = 0; day < days; day++)
			schedules.add(schedule);
		PlanSchedulesRequest planSchedulesRequest = new PlanSchedulesRequest(schedules);

		// GIVEN - DaySchedule
		List<DaySchedule> daySchedules = new ArrayList<>();
		for (int day = 0; day < days; day++) {
			daySchedules.add(DaySchedule.builder()
					.id((long) day)
					.placeItemList(new ArrayList<>())
					.build());
		}

		// Stub
		Plan plan = Plan.builder()
				.id(planId)
				.dayScheduleList(daySchedules)
				.build();
		// PlanRepository
		given(planRepository.findById(planId)).willReturn(Optional.of(plan));
		given(planRepository.findById(notExistPlanId)).willReturn(Optional.empty());
		// PlaceRepository
		given(placeRepository.findById(anyLong())).willReturn(Optional.empty());
		// RegionService
		Region region = new Region(1L, "STATE", "CITY", null, null, null);
		given(regionService.getRegionByAddress(anyString())).willReturn(Optional.of(region));

		// WHEN - THEN
		assertAll(
				// 성공 케이스 - 1 - 문제 없이 장소 목록 수정을 완료
				() -> assertDoesNotThrow(() -> planService.updatePlacesInPlan(planId, planSchedulesRequest)),
				// 실패 케이스 - 1 - 장소 목록을 수정할 플랜이 존재하지 않음
				() -> assertThrows(NotFoundException.class, () -> planService.updatePlacesInPlan(notExistPlanId, planSchedulesRequest))
		);

	}
}