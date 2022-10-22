/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : UsersApi
 * author         : 신우진
 * date           : 2022/08/28
 * description    : 사용자 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto.OpinionWithPlaceResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.UserDto.UserProfileRequest;
import com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@RequestMapping("/users")
public interface UsersApi {
	@Operation(summary = "사용자 프로필 조회", description = "사용자의 프로필을 조회합니다.", tags = {"Users"})
	@ApiResponses(
			@ApiResponse(responseCode = "404", description = "정보가 존재하지 않을 때", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))})
	)
	@GetMapping("/{userId}/profile")
	UserProfileResponse getUserProfile(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId
	) throws NotFoundException;

	@Operation(summary = "사용자 프로필 수정", description = "사용자의 프로필을 수정합니다.", tags = {"Users"})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "수정 성공 시, 프로필 이미지를 등록·수정·제거하는 URL 응답"),
			@ApiResponse(responseCode = "403", description = "권한이 없는 경우", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))})
	})
	@PutMapping("/{userId}/profile")
	Map<String, String> updateUserProfile(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.DEFAULT, description = "프로필 정보", required = true) @Validated @RequestBody UserProfileRequest request,
			BindingResult bindingResult,
			@ApiIgnore LoginUser loginUser
	) throws BadRequestException, ForbiddenException, NotFoundException;

	@Operation(summary = "작성한 여행 On 조회", description = "사용자가 작성한 여행 On 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/{userId}/travel-ons")
	List<TravelOnSimpleResponse> getUserTravelOns(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) @Validated PageRequest pageRequest,
			BindingResult bindingResult
	) throws BadRequestException;

	@Operation(summary = "작성한 답변 조회", description = "사용자가 작성한 답변 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/{userId}/opinions")
	List<OpinionWithPlaceResponse> getUserOpinions(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) @Validated PageRequest pageRequest,
			BindingResult bindingResult
	) throws NotFoundException, BadRequestException;

	@Operation(summary = "랭킹 목록 조회", description = "랭킹 상위 N명의 사용자 목록을 조회합니다.", tags = {"Users"})
	@GetMapping("/ranking")
	List<UserProfileResponse> getRanking();

	@Operation(summary = "회원탈퇴", tags = {"Users"})
	@DeleteMapping("/{userId}")
	void deleteUser(
			@Parameter(in = ParameterIn.PATH, description = "사용자 ID", required = true) @PathVariable long userId,
			@ApiIgnore LoginUser loginUser
	) throws NotFoundException, ForbiddenException;
}
