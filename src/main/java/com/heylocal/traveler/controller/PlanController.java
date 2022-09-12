package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlansApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.PlanListResponse;
import com.heylocal.traveler.dto.PlanDto.PlanPlacesRequest;
import com.heylocal.traveler.dto.PlanDto.PlanPlacesResponse;
import com.heylocal.traveler.dto.PlanDto.PlanRequest;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Plans")
@RequiredArgsConstructor
@RestController
public class PlanController implements PlansApi {
	private final PlanService planService;

	/**
	 * <pre>
	 * 마이 플랜 목록을 조회합니다.
	 * @param loginUser 로그인 되어 있는 사용자의 정보
	 * @return 작성한 플랜 정보
	 * </pre>
	 */
	@Override
	public PlanListResponse getPlans(LoginUser loginUser) {
		// 로그인 정보 확인
		long userId = loginUser.getId();

		// 마이 플랜 조회
		return planService.getPlans(userId);
	}

	/**
	 * <pre>
	 * 플랜을 등록합니다.
	 * @param request 플랜 정보
	 * @return
	 * </pre>
	 */
	@Override
	public ResponseEntity<Void> createPlan(PlanRequest request) {
		return null;
	}

	/**
	 * <pre>
	 * 플랜을 수정합니다.
	 * @param planId 플랜 ID
	 * @param request 플랜 정보
	 * @return
	 * </pre>
	 */
	@Override
	public ResponseEntity<Void> updatePlan(long planId, PlanRequest request) {
		return null;
	}

	/**
	 * <pre>
	 * 플랜을 삭제합니다.
	 * @param planId 플랜 ID
	 * @return
	 * </pre>
	 */
	@Override
	public ResponseEntity<Void> deletePlan(long planId) {
		return null;
	}

	/**
	 * <pre>
	 * 플랜의 장소 목록을 반환합니다.
	 * @param planId 플랜 ID
	 * @return 장소 목록
	 * @throws NotFoundException
	 * </pre>
	 */
	@Override
	public List<PlanPlacesResponse> getPlacesInPlan(long planId) throws NotFoundException {
		return planService.getPlacesInPlan(planId);
	}

	/**
	 * <pre>
	 * 플랜의 장소 목록을 수정합니다.
	 * @param planId 플랜 ID
	 * @param request 장소 목록
	 * @return
	 * </pre>
	 */
	@Override
	public void updatePlacesInPlan(long planId, List<PlanPlacesRequest> request) {
		planService.updatePlacesInPlan(planId, request);
	}
}
