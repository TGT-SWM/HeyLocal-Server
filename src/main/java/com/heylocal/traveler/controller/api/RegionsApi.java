package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.RegionDto.RegionResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/regions")
public interface RegionsApi {
	@Operation(summary = "지역 목록 조회", description = "전체 지역 목록을 조회합니다.", tags = {"Regions"})
	@GetMapping()
	List<RegionResponse> getRegions();
}
