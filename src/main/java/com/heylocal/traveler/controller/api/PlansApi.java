/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : PlansApi
 * author         : 신우진
 * date           : 2022/08/28
 * description    : 플랜 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.*;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequestMapping("/plans")
public interface PlansApi {
	@Operation(summary = "작성한 플랜 조회", description = "작성한 플랜의 목록을 조회합니다.", tags = {"Plans"})
	@GetMapping()
	PlanListResponse getPlans(
			@ApiIgnore LoginUser loginUser
	);

	@Operation(summary = "플랜 등록", description = "플랜을 등록합니다.", tags = {"Plans"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "플랜 등록 성공"),
			@ApiResponse(responseCode = "400", description = "- `ALREADY_EXISTS`: 이미 플랜이 있는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
			@ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 권한이 없는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 여행 On이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@PostMapping()
	void createPlan(
			@Parameter(in = ParameterIn.DEFAULT, description = "플랜 정보", required = true) @RequestBody PlanCreateRequest request,
			@ApiIgnore LoginUser loginUser
	) throws NotFoundException, ForbiddenException, BadRequestException;

	@Operation(summary = "플랜 수정", description = "플랜을 수정합니다.", tags = {"Plans"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "플랜 수정 성공"),
			@ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 권한이 없는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 플랜이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@PutMapping("/{planId}")
	void updatePlan(
			@Parameter(in = ParameterIn.PATH, description = "플랜 ID", required = true) @PathVariable long planId,
			@Parameter(in = ParameterIn.DEFAULT, description = "플랜 정보", required = true) @RequestBody PlanUpdateRequest request,
			@ApiIgnore LoginUser loginUser
	) throws ForbiddenException, NotFoundException;

	@Operation(summary = "플랜 삭제", description = "플랜을 삭제합니다.", tags = {"Plans"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "플랜 삭제 성공"),
			@ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 권한이 없는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 플랜이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@DeleteMapping("/{planId}")
	void deletePlan(
			@Parameter(in = ParameterIn.PATH, description = "플랜 ID", required = true) @PathVariable long planId,
			@ApiIgnore LoginUser loginUser
	) throws ForbiddenException, NotFoundException;

	/*
	 * 아래는 플랜 내 장소들에 대한 컨트롤러입니다.
	 */

	@Operation(summary = "플랜의 장소 목록 조회", description = "플랜의 장소 목록 조회", tags = {"Plans"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "플랜의 장소 목록 조회 성공"),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@GetMapping("/{planId}/places")
	List<PlanPlacesResponse> getPlacesInPlan(
			@Parameter(in = ParameterIn.PATH, description = "플랜 ID", required = true) @PathVariable long planId
	) throws NotFoundException;

	@Operation(summary = "플랜의 장소 목록 수정", description = "플랜의 장소 목록 수정", tags = {"Plans"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "플랜의 장소 목록 수정 성공"),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 플랜이 존재하지 않는 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@PutMapping("/{planId}/places")
	void updatePlacesInPlan(
			@Parameter(in = ParameterIn.PATH, description = "플랜 ID", required = true) @PathVariable long planId,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true) @RequestBody PlanSchedulesRequest request
	) throws NotFoundException;
}
