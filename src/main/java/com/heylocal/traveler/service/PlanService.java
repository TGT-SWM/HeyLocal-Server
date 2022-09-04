package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.dto.PlanDto;
import com.heylocal.traveler.dto.PlanDto.PlanListResponse;
import com.heylocal.traveler.dto.PlanDto.PlanPlacesResponse;
import com.heylocal.traveler.dto.PlanDto.PlanResponse;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
	private final PlanRepository planRepository;

	private final Clock clock;

	/**
	 * <pre>
	 * 작성한 스케줄 정보를 반환합니다.
	 * @param userId 사용자의 아이디
	 * @return 작성한 스케줄 정보
	 * </pre>
	 */
	public PlanListResponse getPlans(long userId) {
		// 작성한 플랜 조회
		List<Plan> plans = planRepository.findByUserId(userId);

		// Plan -> PlanResponse 변환
		List<PlanResponse> planResponses = plans.stream()
				.map(PlanResponse::new)
				.collect(Collectors.toList());

		// 플랜을 날짜에 따라 분류
		LocalDate today = LocalDate.now(clock);
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

	/**
	 * <pre>
	 * 해당 플랜에 포함된 장소 리스트를 일자별로 나누어 반환
	 * @param planId 플랜 ID
	 * @return
	 * </pre>
	 */
	public List<PlanPlacesResponse> getPlacesInPlan(long planId) throws BadArgumentException {
		// Plan 조회
		// Plan이 존재하지 않는 경우에는 예외 발생
		Optional<Plan> optPlan = planRepository.findById(planId);
		if (optPlan.isEmpty())
			throw new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 플랜입니다.");
		Plan plan = optPlan.get();

		// DTO 변환
		// List<DaySchedule> -> List<PlanPlacesReponse>
		List<DaySchedule> daySchedules = plan.getDayScheduleList();
		return daySchedules.stream()
				.map(PlanPlacesResponse::new)
				.collect(Collectors.toList());
	}
}
