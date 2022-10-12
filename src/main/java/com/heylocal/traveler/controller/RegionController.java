package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RegionsApi;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.RegionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : RegionController
 * author         : 신우진
 * date           : 2022/09/03
 * description    : 지역 API 컨트롤러
 */

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
	public List<RegionResponse> getRegions(Long regionId) throws NotFoundException {
		List<RegionResponse> response;

		if (regionId == null) { //만약 id가 없다면
			response = regionService.inquiryAllRegions();
		} else { //만약 id가 있다면
			response = new ArrayList<>();
			response.add(regionService.inquiryRegions(regionId));
		}

		return response;
	}
}
