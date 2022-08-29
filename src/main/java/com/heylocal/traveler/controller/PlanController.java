package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlansApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Plans")
@RestController
public class PlanController implements PlansApi {
	/**
	 * @return
	 */
	@Override
	public PlanListResponse getPlans(LoginUser loginUser) {
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Void> createPlan(PlanRequest request) {
		return null;
	}

	/**
	 * @param planId
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Void> updatePlan(long planId, PlanRequest request) {
		return null;
	}

	/**
	 * @param planId
	 * @return
	 */
	@Override
	public ResponseEntity<Void> deletePlan(long planId) {
		return null;
	}

	/**
	 * @param planId
	 * @param day
	 * @return
	 */
	@Override
	public List<PlanPlacesResponse> getPlacesInPlan(long planId, int day) {
		return null;
	}

	/**
	 * @param planId
	 * @param day
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Void> updatePlaceInPlan(long planId, int day, PlanPlacesRequest request) {
		return null;
	}
}
