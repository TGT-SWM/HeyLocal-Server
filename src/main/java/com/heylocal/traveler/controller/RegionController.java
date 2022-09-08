package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RegionsApi;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.exception.service.BadArgumentException;
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
	 */
	@Override
	public List<RegionResponse> getRegions(String state) throws NotFoundException {
		List<RegionResponse> response;

		try {
			response = regionService.inquiryRegions(state);
		} catch (BadArgumentException e) {
			throw new NotFoundException(e.getCode(), e.getDescription());
		}

		return response;
	}
}
