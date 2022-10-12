/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : RegionsApi
 * author         : 신우진
 * date           : 2022/08/28
 * description    : 지역 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.dto.RegionDto.RegionResponse;
import com.heylocal.traveler.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/regions")
public interface RegionsApi {
	@Operation(summary = "지역 목록 조회", description = "전체 지역 목록을 조회합니다.", tags = {"Regions"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "전체 지역 목록 조회 성공"),
			@ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
	})
	@GetMapping()
	List<RegionResponse> getRegions(
			@Parameter(in = ParameterIn.QUERY, description = "조회할 Region의 ID\n\n만약 아무 값도 전달하지 않는다면, 모든 지역을 조회한다.", required = false) Long regionId
	) throws NotFoundException;
}
