/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : UserController
 * author         : 신우진
 * date           : 2022/09/03
 * description    : 사용자 API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.UsersApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.SignupCode;
import com.heylocal.traveler.service.AuthService;
import com.heylocal.traveler.service.OpinionService;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.service.UserService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.heylocal.traveler.dto.OpinionDto.OpinionWithPlaceResponse;
import static com.heylocal.traveler.dto.UserDto.*;

@Tag(name = "Users")
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
	@Value("#{propertiesToUft8['heylocal.signup.pattern.nickname']}") //UTF-8로 값 가져오기
	private String nicknamePattern;
	private final TravelOnService travelOnService;
	private final UserService userService;
	private final OpinionService opinionService;
	private final AuthService authService;
	private final BindingErrorMessageProvider errorMessageProvider;

	/**
	 * 사용자 프로필 조회 핸들러
	 * @param userId
	 * @return
	 */
	@Override
	public UserProfileResponse getUserProfile(long userId) throws NotFoundException {
		UserProfileResponse response;

		response = userService.inquiryUserProfile(userId); //ProfileResponse DTO 구하기

		return response;
	}

	/**
	 * 사용자 프로필 수정 핸들러
	 * @param userId
	 * @param request
	 * @return 프로필 이미지 수정 Presigned URL
	 */
	@Override
	public Map<String, String> updateUserProfile(long userId, UserProfileRequest request,
																							 BindingResult bindingResult, LoginUser loginUser) throws BadRequestException, ForbiddenException, NotFoundException {
		//수정 권한 검증
		boolean canUpdate = userService.canUpdateProfile(userId, loginUser);
		if (!canUpdate) throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "프로필 수정 권한이 없습니다.");

		//기본 값 검증
		if (bindingResult.hasFieldErrors()) {
			String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
			throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
		}

		//닉네임 형식 검증
		validateNicknameFormat(request.getNickname());

		//프로필 업데이트
		userService.updateProfile(userId, request);

		//프로필 이미지 업로드·제거 Presigned URL 반환
		return userService.getImgUpdatePresignedUrl(userId);
	}

	/**
	 * 특정 사용자가 작성한 여행 On의 목록을 페이징하여 조회합니다.
	 * @param userId 사용자 ID
	 * @param pageRequest 요청하는 페이지 정보
	 * @return 여행 On 목록
	 */
	@Override
	public List<TravelOnSimpleResponse> getUserTravelOns(long userId, PageRequest pageRequest, BindingResult bindingResult) throws BadRequestException {
		if (bindingResult.hasFieldErrors()) {
			String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
			throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
		}

		return travelOnService.inquirySimpleTravelOns(userId, pageRequest);
	}

	/**
	 * 특정 사용자가 작성한 답변 목록을 페이징하여 조회하는 핸들러
	 * @param userId 사용자 ID
	 * @param pageRequest 요청 페이지 정보
	 * @return
	 */
	@Override
  public List<OpinionWithPlaceResponse> getUserOpinions(long userId, PageDto.PageRequest pageRequest, BindingResult bindingResult) throws NotFoundException, BadRequestException {
		if (bindingResult.hasFieldErrors()) {
			String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
			throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
		}

		return opinionService.inquiryOpinionsByUserId(userId, pageRequest);
	}

	/**
	 * 노하우 랭킹을 조회하는 핸들러
	 * @return 랭킹 순위
	 */
	@Override
	public List<UserProfileResponse> getRanking() {
		return userService.inquiryUserProfileByKnowHowDesc();
	}

	/**
	 * 회원탈퇴 핸들러
	 * @param userId
	 */
	@Override
	public void deleteUser(long userId, LoginUser loginUser) throws NotFoundException, ForbiddenException {
		//유효한 id 인지 조회
		UserResponse user = userService.inquiryUser(userId);

		//로그인한 사용자와 다른 id 라면
		if (user.getId() != loginUser.getId()) {
			throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "다른 계정을 삭제할 수 없습니다.");
		}

		//사용자 익명화
		userService.anonymizeUser(userId);

		//Redis에서 토큰 제거 (로그아웃 처리)
		authService.removeTokens(userId);
	}

	/**
	 * <pre>
	 * 닉네임 형식 검증
	 * 숫자 + 영어 조합, 2자 이상, 20자 이하
	 * </pre>
	 * @param nickname 검증할 닉네임
	 * @return 조건에 부합하면 true 반환
	 * @throws BadRequestException 조건에 부합하지 않는다면, 발생하는 예외
	 */
	private boolean validateNicknameFormat(String nickname) throws BadRequestException {
		if (!Pattern.matches(nicknamePattern, nickname)) {
			throw new BadRequestException(SignupCode.WRONG_NICKNAME_FORMAT, "닉네임 형식이 잘못되었습니다. 닉네임은 숫자 + 영어 조합, 2자 이상, 20자 이하입니다.");
		}

		return true;
	}
}
