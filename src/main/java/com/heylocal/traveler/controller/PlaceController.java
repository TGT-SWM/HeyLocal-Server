package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.PlacesApi;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.service.OpinionService;
import com.heylocal.traveler.service.PlaceService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import static com.heylocal.traveler.dto.PageDto.PageRequest;
import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import static com.heylocal.traveler.dto.PlaceDto.PlaceWithOpinionSizeResponse;

/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : PlaceController
 * author         : 신우진
 * date           : 2022/09/03
 * description    : 장소 API 컨트롤러
 */

@Tag(name = "Places")
@RestController
@RequiredArgsConstructor
public class PlaceController implements PlacesApi {
	private final BindingErrorMessageProvider errorMessageProvider;
	private final PlaceService placeService;
	private final OpinionService opinionService;

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
	 * 해당 장소를 선택한 답변 목록 조회
	 * @param placeId
	 * @param pageRequest
	 * @return
	 */
	@Override
	public List<OpinionResponse> getPlaceOpinions(long placeId, PageRequest pageRequest,
																												 BindingResult bindingResult) throws BadRequestException {
		if (bindingResult.hasFieldErrors()) {
			String errMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
			throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
		}

		List<OpinionResponse> opinionResponseList = opinionService.inquiryOpinionsByPlace(placeId, pageRequest);
		return opinionResponseList;
	}

	/**
	 * 인기 장소 조회 핸들러
	 * @return 인기가 높은 순으로 정렬
	 */
	@Override
	public List<PlaceWithOpinionSizeResponse> getHotPlaces() {
		return placeService.inquiryMostOpinedPlace(null);
	}
}
