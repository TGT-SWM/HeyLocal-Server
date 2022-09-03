package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlacesApi;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.dto.PlaceDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Places")
@RestController
public class PlaceController implements PlacesApi {
	/**
	 * @param placeId
	 * @return
	 */
	@Override
	public PlaceDto.PlaceResponse getPlace(long placeId) {
		return null;
	}

	/**
	 * @param placeId
	 * @param pageRequest
	 * @return
	 */
	@Override
	public List<OpinionDto.OpinionResponse> getPlaceOpinions(long placeId, PageDto.PageRequest pageRequest) {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public List<PlaceDto.PlaceResponse> getHotPlaces() {
		return null;
	}
}
