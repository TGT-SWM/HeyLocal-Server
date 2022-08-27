package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.*;
import com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.UserDto.UserProfileRequest;
import com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
public interface UsersApi {
	@Operation(summary = "사용자 프로필 조회", description = "사용자의 프로필을 조회합니다.", tags = {"Users"})
	@GetMapping("/{userId}/profile")
	@ApiResponses(
			@ApiResponse(responseCode = "404", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))})
	)
	UserProfileResponse getUserProfile(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId
	);

	// TODO: userId를 파라미터로 받지 않고, 로그인된 유저에 대해 요청을 처리할 것인지 논의
	@Operation(summary = "사용자 프로필 수정", description = "사용자의 프로필을 수정합니다.", tags = {"Users"})
	@PutMapping("/{userId}/profile")
	@ApiResponses(
			@ApiResponse(responseCode = "403", description = "", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))})
	)
	ResponseEntity<Void> updateUserProfile(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.DEFAULT, description = "프로필 정보", required = true) @RequestBody UserProfileRequest request
	);

	@Operation(summary = "작성한 여행 On 조회", description = "사용자가 작성한 여행 On 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/{userId}/travelons")
	List<TravelOnSimpleResponse> getUserTravelOns(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) PageRequest pageRequest
	);

	@Operation(summary = "작성한 답변 조회", description = "사용자가 작성한 답변 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/{userId}/opinions")
	List<OpinionResponse> getUserOpinions(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) PageRequest pageRequest
	);

	@Operation(summary = "랭킹 목록 조회", description = "랭킹 상위 N명의 사용자 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/ranking")
	List<UserProfileResponse> getRanking();
}
