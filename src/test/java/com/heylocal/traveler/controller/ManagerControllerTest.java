package com.heylocal.traveler.controller;

import com.heylocal.traveler.domain.post.Post;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileResponse;
import com.heylocal.traveler.dto.ManagerDto.ManagerReviewResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.service.ManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ManagerControllerTest {
	@Mock
	private ManagerService managerService;

	@InjectMocks
	private ManagerController managerController;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("매니저 프로필 조회 컨트롤러")
	void managersManagerIdProfileGetTest() {
		// GIVEN
		long id = 1L;
		long notExistsId = id + 1;

		Manager manager = createManager(id);
		List<Post> postList = new ArrayList<>();
		ManagerProfileResponse response = ManagerProfileResponse.from(manager, postList);
		given(managerService.findProfileById(id)).willReturn(Optional.ofNullable(response));

		// WHEN - THEN
		// 성공 케이스 - 1 - 저장되어 있는 프로필을 성공적으로 조회하는 경우
		Assertions.assertDoesNotThrow(() -> {
			ManagerProfileResponse actual = managerController.managersManagerIdProfileGet(id);
			assertThat(actual.getId()).isEqualTo(id);
		});

		// 실패 케이스 - 1 - 매니저 프로필 조회에 실패한 경우
		Assertions.assertThrows(
				NotFoundException.class,
				() -> managerController.managersManagerIdProfileGet(notExistsId)
		);
	}

	@Test
	@DisplayName("매니저 리뷰 조회 컨트롤러")
	void managersManagerReviews() {
		// GIVEN
		// 매니저 리뷰 등록하도록 수정
		long id = 1L;
		long notExistsId = id + 1;
		int page = 1;
		int pageSize = 10;
		PageRequest pageRequest = new PageRequest(page, pageSize);

		// Mock - 조회 결과가 존재하는 경우
		List<ManagerReviewResponse> response = new ArrayList<>();
		for (int i = 0; i < pageSize; i++) {
			response.add(new ManagerReviewResponse());
		}
		given(managerService.findReviews(id, pageRequest)).willReturn(response);

		// Mock - 조회 결과가 존재하지 않는 경우
		given(managerService.findReviews(notExistsId, pageRequest)).willReturn(new ArrayList<>());

		// WHEN
		List<ManagerReviewResponse> result = managerController.managersManagerReviews(id, pageRequest);
		List<ManagerReviewResponse> notExistsResult = managerController.managersManagerReviews(notExistsId, pageRequest);

		// THEN
		Assertions.assertAll(
				// 성공 케이스 - 1 - 저장되어 있는 매니저 리뷰를 조회하는 경우
				() -> assertThat(result.size()).isEqualTo(response.size()),
				// 실패 케이스 - 1 - 매니저 리뷰 조회 결과가 존재하지 않는 경우
				() -> Assertions.assertTrue(notExistsResult.isEmpty())
		);


	}

	// 매니저 객체 생성해 반환
	private Manager createManager(Long id) {
		return Manager.builder()
				.id(id)
				.realName("김현지")
				.userProfile(new ManagerProfile())
				.build();
	}
}