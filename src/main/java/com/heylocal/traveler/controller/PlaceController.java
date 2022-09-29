package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlacesApi;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.PlaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.heylocal.traveler.dto.PlaceDto.*;

@Tag(name = "Places")
@RestController
@RequiredArgsConstructor
public class PlaceController implements PlacesApi {
	private final PlaceService placeService;

	/**
	 * 장소 정보를 조회하는 핸들러
	 * @param placeId 조회할 장소의 ID
	 * @return
	 */
	@Override
	public PlaceResponse getPlace(long placeId) throws NotFoundException {
		PlaceResponse response = placeService.inquiryPlace(placeId);
		return response;
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
	public List<PlaceResponse> getHotPlaces() {
		return null;
	}
}
