package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import com.heylocal.traveler.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/places")
public interface PlacesApi {
	@Operation(summary = "장소 정보 조회", description = "장소에 대한 상세 정보를 조회합니다.", tags = {"Places"})
	@GetMapping("/{placeId}")
	PlaceResponse getPlace(
			@Parameter(in = ParameterIn.PATH, description = "장소 ID", required = true) @PathVariable long placeId
	) throws NotFoundException;

	@Operation(summary = "장소에 대한 답변 조회", description = "장소에 대한 여행 On 답변 목록을 조회합니다.", tags = {"Places"})
	@GetMapping("/{placeId}/opinions")
	List<OpinionResponse> getPlaceOpinions(
			@Parameter(in = ParameterIn.PATH, description = "장소 ID", required = true) long placeId,
			@Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) PageRequest pageRequest
	);

	@Operation(summary = "인기 장소 조회", description = "상위 N개의 인기 장소 목록을 조회합니다.", tags = {"Places"})
	@GetMapping("/hot")
	List<PlaceResponse> getHotPlaces();
}
