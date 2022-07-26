/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : PlanController
 * author         : 신우진
 * date           : 2022/08/29
 * description    : 플랜 API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlansApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.*;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
	 * @param loginUser 로그인 사용자 정보
	 * @param request 플랜 정보
	 * </pre>
	 */
	@Override
	public void createPlan(PlanCreateRequest request, LoginUser loginUser) throws NotFoundException, ForbiddenException, BadRequestException {
		long userId = loginUser.getId();
		long travelOnId = request.getTravelOnId();
		planService.createPlan(userId, travelOnId);
	}

	/**
	 * <pre>
	 * 플랜을 수정합니다.
	 * @param planId 플랜 ID
	 * @param request 플랜 정보
	 * @param loginUser 로그인 사용자 정보
	 * </pre>
	 */
	@Override
	public void updatePlan(long planId, PlanUpdateRequest request, LoginUser loginUser) throws ForbiddenException, NotFoundException {
		long userId = loginUser.getId();
		planService.updatePlan(planId, userId, request);
	}

	/**
	 * <pre>
	 * 플랜을 삭제합니다.
	 * @param planId 플랜 ID
	 * @param loginUser 로그인 사용자 정보
	 * </pre>
	 */
	@Override
	public void deletePlan(long planId, LoginUser loginUser) throws ForbiddenException, NotFoundException {
		long userId = loginUser.getId();
		planService.deletePlan(planId, userId);
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
	 * </pre>
	 */
	@Override
	public void updatePlacesInPlan(long planId, PlanSchedulesRequest request) throws NotFoundException {
		planService.updatePlacesInPlan(planId, request);
	}

	/**
	 * <pre>
	 * 답변을 채택하여 내 스케줄에 해당 장소를 추가합니다.
	 * @param planId 플랜 ID
	 * @param day 일자
	 * @param request 답변 정보
	 * </pre>
	 */
	@Override
	public void addPlaceFromOpinion(long planId, int day, AddPlaceRequest request) throws NotFoundException {
		long opinionId = request.getOpinionId();
		planService.addPlaceFromOpinion(planId, day, opinionId);
	}
}
