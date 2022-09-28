package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RegionsApi;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.RegionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

@Tag(name = "Regions")
@RestController
@RequiredArgsConstructor
public class RegionController implements RegionsApi {
	private final RegionService regionService;

	/**
	 * 지역 정보 조회 핸들러
	 * @return
	 * @throws NotFoundException
	 */
	@Override
	public List<RegionResponse> getRegions(String state) throws NotFoundException {
		List<RegionResponse> response;

		if (state == null || state.isEmpty()) { //만약 state가 없다면
			response = regionService.inquiryAllRegions();
		} else { //만약 state가 있다면
			response = regionService.inquiryRegions(state);
		}

		return response;
	}
}
