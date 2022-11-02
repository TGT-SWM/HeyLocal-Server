package com.heylocal.traveler.controller;

import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.AuthService;
import com.heylocal.traveler.service.OpinionService;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.service.UserService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.dto.UserDto.UserProfileRequest;
import static com.heylocal.traveler.dto.UserDto.UserResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class UserControllerTest {
	@Mock
	private TravelOnService travelOnService;
	@Mock
	private UserService userService;
	@Mock
	private OpinionService opinionService;
	@Mock
	private AuthService authService;
	@Mock
	private BindingErrorMessageProvider errorMessageProvider;
	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(userController, "nicknamePattern", "^[a-zA-Z0-9]{2,20}$");
	}

	@Test
	@DisplayName("작성한 여행 On 조회")
	void getUserTravelOnsTest() throws BadRequestException {
		// GIVEN (Request)
		long userId = 1L;
		long noTravelOnUserId = userId + 1;
		PageRequest pageRequest = new PageRequest(null, 10);

		// GIVEN (TravelOnSimpleResponse)
		final int travelOnCnt = 10;
		List<TravelOnSimpleResponse> travelOnResp = new ArrayList<>();
		for (int i = 0; i < travelOnCnt; i++)
			travelOnResp.add(new TravelOnSimpleResponse());

		given(travelOnService.inquirySimpleTravelOns(userId, pageRequest)).willReturn(travelOnResp);
		given(travelOnService.inquirySimpleTravelOns(noTravelOnUserId, pageRequest)).willReturn(new ArrayList<TravelOnSimpleResponse>());

		// GIVEN (BindingResult)
		willReturn(false).given(bindingResult).hasFieldErrors();

		// WHEN
		List<TravelOnSimpleResponse> successResp = userController.getUserTravelOns(userId, pageRequest, bindingResult);
		List<TravelOnSimpleResponse> failResp = userController.getUserTravelOns(noTravelOnUserId, pageRequest, bindingResult);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 여행 On 조회 성공
				() -> Assertions.assertThat(successResp.size()).isEqualTo(travelOnCnt),
				// 실패 케이스 - 1 - 작성한 여행 On이 없는 경우
				() -> Assertions.assertThat(failResp).isEmpty()
		);
	}

	@Test
	@DisplayName("사용자 프로필 조회 핸들러")
	void getUserProfileTest() throws NotFoundException {
		//GIVEN
		long existUserId = 1;
		long notExistUserId = 2;

		//Mock 행동 정의 - userService
		willThrow(NotFoundException.class).given(userService).inquiryUserProfile(notExistUserId);

		//WHEN

		//THEN
		assertAll(
				//성공 케이스
				() -> assertDoesNotThrow(() -> userController.getUserProfile(existUserId)),
				//실패 케이스
				() -> assertThrows(NotFoundException.class, () -> userController.getUserProfile(notExistUserId))
		);
	}

	@Test
	@DisplayName("사용자 프로필 수정 핸들러 - 성공 케이스")
	void updateUserProfileSucceedTest() {
		//GIVEN
		long targetUserId = 1L;
		LoginUser loginUser = LoginUser.builder().id(targetUserId).build();
		String validNickname = "validNickname";
		UserProfileRequest request = UserProfileRequest.builder()
				.nickname(validNickname)
				.build();

		//Mock 행동 정의 - userService
		willReturn(true).given(userService).canUpdateProfile(targetUserId, loginUser);

		//Mock 행동 정의 - bindingResult
		willReturn(false).given(bindingResult).hasFieldErrors();

		//WHEN

		//THEN
		//성공 케이스
		assertDoesNotThrow(() -> userController.updateUserProfile(targetUserId, request, bindingResult, loginUser));
	}

	@Test
	@DisplayName("사용자 프로필 수정 핸들러 - 프로필 수정 대상과 로그인 사용자가 다른 경우")
	void updateUserProfileForbiddenTest() {
		//GIVEN
		long targetUserId = 1L;
		LoginUser loginUser = LoginUser.builder().id(2L).build();
		String validNickname = "validNickname";
		UserProfileRequest request = UserProfileRequest.builder()
				.nickname(validNickname)
				.build();

		//Mock 행동 정의 - userService
		willReturn(false).given(userService).canUpdateProfile(targetUserId, loginUser);

		//WHEN

		//THEN
		assertThrows(ForbiddenException.class, () -> userController.updateUserProfile(targetUserId, request, bindingResult, loginUser));
	}

	@Test
	@DisplayName("사용자 프로필 수정 핸들러 - 프로필 수정 Input 형식이 잘못된 경우")
	void updateUserProfileWrongValueTest() {
		//GIVEN
		long targetUserId = 1L;
		LoginUser loginUser = LoginUser.builder().id(2L).build();
		String validNickname = "validNickname";
		int invalidRegionId = -1;
		UserProfileRequest request = UserProfileRequest.builder()
				.activityRegionId(invalidRegionId)
				.nickname(validNickname)
				.build();

		//Mock 행동 정의 - userService
		willReturn(true).given(userService).canUpdateProfile(targetUserId, loginUser);

		//Mock 행동 정의 - bindingResult
		willReturn(true).given(bindingResult).hasFieldErrors();

		//Mock 행동 정의 - errorMessageProvider
		willReturn("지역 아이디는 0 이상이어야 합니다.").given(errorMessageProvider).getFieldErrMsg(bindingResult);

		//WHEN

		//THEN
		assertThrows(BadRequestException.class, () -> userController.updateUserProfile(targetUserId, request, bindingResult, loginUser));
	}

	@Test
	@DisplayName("사용자 프로필 수정 핸들러 - 닉네임 형식이 잘못된 경우")
	void updateUserProfileWrongNicknameFormTest() {
		//GIVEN
		long targetUserId = 1L;
		LoginUser loginUser = LoginUser.builder().id(2L).build();
		String invalidNickname = "invalidNickname!@#$$%";
		UserProfileRequest request = UserProfileRequest.builder()
				.nickname(invalidNickname)
				.build();

		//Mock 행동 정의 - userService
		willReturn(true).given(userService).canUpdateProfile(targetUserId, loginUser);

		//Mock 행동 정의 - bindingResult
		willReturn(false).given(bindingResult).hasFieldErrors();

		//WHEN

		//THEN
		assertThrows(BadRequestException.class, () -> userController.updateUserProfile(targetUserId, request, bindingResult, loginUser));
	}

	@Test
	@DisplayName("특정 사용자가 작성한 답변 목록 조회 핸들러")
	void getUserOpinionsTest() {
		//GIVEN
		long targetUserId = 1L;
		PageRequest validPageRequest = PageRequest.builder()
				.lastItemId(null)
				.size(1)
				.build();
		PageRequest invalidPageRequest = PageRequest.builder()
				.lastItemId(null)
				.size(0)
				.build();

		//Mock 행동 정의 - bindingResult
		willReturn(false).willReturn(true).given(bindingResult).hasFieldErrors();

		//WHEN

		//THEN
		assertAll(
				//성공 케이스 - 페이징 요청이 올바른 경우
				() -> assertDoesNotThrow(() -> userController.getUserOpinions(targetUserId, validPageRequest, bindingResult)),
				//실패 케이스 - 페이징 요청이 올바르지 않은 경우
				() -> assertThrows(BadRequestException.class, () -> userController.getUserOpinions(targetUserId, invalidPageRequest, bindingResult))
		);

	}

	@Test
	@DisplayName("랭킹 조회 핸들러")
	void getRankingTest() {
	  //GIVEN

	  //WHEN

	  //THEN
	  assertDoesNotThrow(() -> userController.getRanking());
	}

	@Test
	@DisplayName("회원탈퇴")
	void deleteUserTest() throws NotFoundException, ForbiddenException {
	  //GIVEN
		long userId = 1L;
		LoginUser loginUser = LoginUser.builder().id(userId).build();

		//Mock 행동 정의 - userService
		UserResponse userResponse = UserResponse.builder().id(userId).build();
		willReturn(userResponse).given(userService).inquiryUser(userId);

	  //WHEN

	  //THEN
		//성공 케이스
		assertDoesNotThrow(() -> userController.deleteUser(userId, loginUser));
	}
}