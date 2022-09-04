package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.PlanDto.PlanListResponse;
import com.heylocal.traveler.repository.PlanRepository;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

class PlanServiceTest {
	@Mock
	private PlanRepository planRepository;

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
	@DisplayName("플랜 내 장소 목록 조회")
	void getPlacesInPlanTest() {
	}
}