package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.UsersApi;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.dto.TravelOnDto;
import com.heylocal.traveler.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Users")
@RestController
public class UserController implements UsersApi {
	/**
	 * @param userId
	 * @return
	 */
	@Override
	public UserDto.UserProfileResponse getUserProfile(long userId) {
		return null;
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
	 * @param userId
	 * @param pageRequest
	 * @return
	 */
	@Override
	public List<TravelOnDto.TravelOnSimpleResponse> getUserTravelOns(long userId, PageDto.PageRequest pageRequest) {
		return null;
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
