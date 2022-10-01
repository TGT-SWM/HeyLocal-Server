package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.UsersApi;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.UserDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
	private final TravelOnService travelOnService;
	private final UserService userService;

	/**
	 * 사용자 프로필 조회 핸들러
	 * @param userId
	 * @return
	 */
	@Override
	public UserDto.UserProfileResponse getUserProfile(long userId) throws NotFoundException {
		return userService.inquiryUserProfile(userId);
	}

	/**
	 * @param userId
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Void> updateUserProfile(long userId, UserDto.UserProfileRequest request) {
		return null;
	}

	/**
	 * 특정 사용자가 작성한 여행 On의 목록을 페이징하여 조회합니다.
	 * @param userId 사용자 ID
	 * @param pageRequest 요청하는 페이지 정보
	 * @return 여행 On 목록
	 */
	@Override
	public List<TravelOnSimpleResponse> getUserTravelOns(long userId, PageRequest pageRequest) {
		return travelOnService.inquirySimpleTravelOns(userId, pageRequest);
	}

	/**
	 * @param userId
	 * @param pageRequest
	 * @return
	 */
	@Override
  public List<OpinionDto.OpinionWithPlaceResponse> getUserOpinions(long userId, PageDto.PageRequest pageRequest) {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public List<UserDto.UserProfileResponse> getRanking() {
		return null;
	}
}
