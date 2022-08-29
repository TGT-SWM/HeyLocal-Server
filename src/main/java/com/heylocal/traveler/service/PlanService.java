package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.dto.PlanDto.*;
import com.heylocal.traveler.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
	private final PlanRepository planRepository;

	/**
	 * <pre>
	 * 작성한 스케줄 정보를 반환합니다.
	 * @param userId 사용자의 아이디
	 * @return 작성한 스케줄 정보
	 * </pre>
	 */
	public PlanListResponse getPlans(long userId) {
		// 작성한 플랜 조회
		List<Plan> plans = planRepository.findAll(userId);

		// Plan -> PlanResponse 변환
		List<PlanResponse> planResponses = plans.stream()
				.map(PlanResponse::new)
				.collect(Collectors.toList());

		// 플랜을 날짜에 따라 분류
		LocalDate today = LocalDate.now();
		List<PlanResponse> past = new ArrayList<>();
		List<PlanResponse> ongoing = new ArrayList<>();
		List<PlanResponse> upcoming = new ArrayList<>();

		planResponses.forEach(planResponse -> {
			LocalDate startDate = planResponse.getStartDate();
			LocalDate endDate = planResponse.getEndDate();

			if (today.isBefore(startDate)) {
				upcoming.add(planResponse);
			} else if (today.isAfter(endDate)) {
				past.add(planResponse);
			} else {
				ongoing.add(planResponse);
			}
		});

		// PlanListResponse 생성 및 반환
		return PlanListResponse.builder()
				.past(past)
				.ongoing(ongoing)
				.upcoming(upcoming)
				.build();
	}
}
