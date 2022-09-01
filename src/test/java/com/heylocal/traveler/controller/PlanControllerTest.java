package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.PlanListResponse;
import com.heylocal.traveler.dto.PlanDto.PlanResponse;
import com.heylocal.traveler.service.PlanService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

class PlanControllerTest {
	@Mock
	private PlanService planService;

	@InjectMocks
	private PlanController planController;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("작성한 플랜 정보 조회")
	void getPlansTest() {
		// GIVEN - 로그인 사용자
		long userId = 1L; // 성공 케이스
		long noPlanUserId = userId + 1; // 실패 케이스
		LoginUser user = new LoginUser(userId);
		LoginUser noPlanUser = new LoginUser(noPlanUserId);

		// GIVEN - 플랜 리스트
		int pastCount = 3;
		int ongoingCount = 1;
		int upcomingCount = 1;
		PlanListResponse response = PlanListResponse.builder()
				.past(createPlanResponseList(pastCount))
				.ongoing(createPlanResponseList(ongoingCount))
				.upcoming(createPlanResponseList(upcomingCount))
				.build();
		PlanListResponse emptyResponse = PlanListResponse.builder()
				.past(new ArrayList<>())
				.ongoing(new ArrayList<>())
				.upcoming(new ArrayList<>())
				.build();

		// 플랜 리스트 Stub
		given(planService.getPlans(userId)).willReturn(response);
		given(planService.getPlans(noPlanUserId)).willReturn(emptyResponse);

		// WHEN
		PlanListResponse successCaseResponse = planController.getPlans(user);
		PlanListResponse failCaseResponse = planController.getPlans(noPlanUser);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 사용자에 대해 정확한 플랜 리스트를 반환하는지
				() -> Assertions.assertThat(successCaseResponse).isEqualTo(response),
				// 실패 케이스 - 1 - 사용자가 작성한 플랜이 없는 경우 빈 리스트를 반환하는지
				() -> Assertions.assertThat(failCaseResponse).isEqualTo(emptyResponse)
		);
	}

	/**
	 * <pre>
	 * 원하는 개수의 PlanResponse를 담은 List를 반환합니다.
	 * @param count PlanResponse의 개수
	 * @return PlanResponse의 List
	 * </pre>
	 */
	private List<PlanResponse> createPlanResponseList(int count) {
		List<PlanResponse> result = new ArrayList<>();
		for (int i = 0; i < count; ++i) {
			result.add(new PlanResponse());
		}
		return result;
	}
}