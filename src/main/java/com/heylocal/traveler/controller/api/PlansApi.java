package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.PlanDto;
import com.heylocal.traveler.dto.PlanDto.PlanPlacesRequest;
import com.heylocal.traveler.dto.PlanDto.PlanPlacesResponse;
import com.heylocal.traveler.dto.PlanDto.PlanRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/plans")
public interface PlansApi {
	@Operation(summary = "작성한 스케줄 조회", description = "작성한 스케줄의 목록을 조회합니다.", tags = {"Plans"})
	@GetMapping()
	PlanDto.PlanListResponse getPlans();

	@Operation(summary = "스케줄 등록", description = "스케줄을 등록합니다.", tags = {"Plans"})
	@PostMapping()
	ResponseEntity<Void> createPlan(
			@Parameter(in = ParameterIn.DEFAULT, description = "스케줄 정보", required = true) PlanRequest request
	);

	@Operation(summary = "스케줄 수정", description = "스케줄을 수정합니다.", tags = {"Plans"})
	@PutMapping("/{planId}")
	ResponseEntity<Void> updatePlan(
			@Parameter(in = ParameterIn.PATH, description = "스케줄 ID", required = true) long planId,
			@Parameter(in = ParameterIn.DEFAULT, description = "스케줄 정보", required = true) PlanRequest request
	);

	@Operation(summary = "스케줄 삭제", description = "스케줄을 삭제합니다.", tags = {"Plans"})
	@DeleteMapping("/{planId}")
	ResponseEntity<Void> deletePlan(
			@Parameter(in = ParameterIn.PATH, description = "스케줄 ID", required = true) long planId
	);

	/*
	 * 아래는 스케줄 내 장소들에 대한 컨트롤러입니다.
	 */

	@Operation(summary = "스케줄의 장소 목록 조회", description = "스케줄의 특정 일자에 해당하는 장소 목록 조회", tags = {"Plans"})
	@GetMapping("/{planId}/{day}/places")
	List<PlanPlacesResponse> getPlacesInPlan(
			@Parameter(in = ParameterIn.PATH, description = "스케줄 ID", required = true) long planId,
			@Parameter(in = ParameterIn.PATH, description = "스케줄 일자", required = true) int day
	);

	@Operation(summary = "스케줄의 장소 목록 수정", description = "스케줄의 특정 일자에 해당하는 장소 목록 수정", tags = {"Plans"})
	@PutMapping("/{planId}/{day}/places")
	ResponseEntity<Void> updatePlaceInPlan(
			@Parameter(in = ParameterIn.PATH, description = "스케줄 ID", required = true) long planId,
			@Parameter(in = ParameterIn.PATH, description = "스케줄 일자", required = true) int day,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true) PlanPlacesRequest request
	);
}
