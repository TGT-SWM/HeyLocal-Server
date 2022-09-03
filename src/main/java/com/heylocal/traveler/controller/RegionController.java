package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RegionsApi;
import com.heylocal.traveler.dto.RegionDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Regions")
@RestController
public class RegionController implements RegionsApi {
	/**
	 * @return
	 */
	@Override
	public List<RegionDto.RegionResponse> getRegions() {
		return null;
	}
}
