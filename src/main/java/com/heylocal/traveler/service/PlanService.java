package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.travel.Travel;
import com.heylocal.traveler.dto.PlanDto.*;
import com.heylocal.traveler.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
		// 작성한 스케줄 조회
		List<Travel> travels = planRepository.findAll(userId);

		// DTO 변환 로직

		return null;
	}
}
