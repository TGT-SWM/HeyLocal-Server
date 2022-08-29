package com.heylocal.traveler.service;

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
	 * @return 작성한 스케줄 정보
	 * </pre>
	 */
	PlanListResponse getPlans() {
		return null;
	}
}
