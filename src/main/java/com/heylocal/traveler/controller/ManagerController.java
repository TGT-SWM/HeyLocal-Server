package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileResponse;
import com.heylocal.traveler.dto.ManagerDto.ManagerReviewResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.Sample;
import com.heylocal.traveler.service.ManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Managers")
@RequiredArgsConstructor
public class ManagerController implements ManagersApi {
	private final ManagerService managerService;

	@Override
	public ResponseEntity<Void> managersManagerIdNotesGet(long managerId, int page) {
		return null;
	}

	@Override
	public ResponseEntity<Void> managersManagerIdNotesNoteIdGet(long managerId, long noteId) {
		return null;
	}

	/**
	 * <pre>
	 * 매니저 프로필 조회
	 * </pre>
	 * @param managerId 조회하고자 하는 매니저의 ID(PK)
	 * @return 매니저 프로필 정보
	 * @throws NotFoundException HTTP 404 오류
	 */
	@Override
	public ManagerProfileResponse managersManagerIdProfileGet(long managerId) throws NotFoundException {
		// 매니저 프로필 조회
		Optional<ManagerProfileResponse> optResponse = managerService.findProfileById(managerId);

		// 매니저 프로필 정보가 없는 경우
		if (optResponse.isEmpty()) {
			throw new NotFoundException("정보가 존재하지 않습니다.");
		}

		return optResponse.get();
	}

	@Override
	public ResponseEntity<Void> managersManagerIdReviewsPost(long managerId, Sample body) {
		return null;
	}

	/**
	 * <pre>
	 * 매니저 리뷰 조회
	 * </pre>
	 * @param managerId 리뷰를 조회하고자 하는 매니저의 ID(PK)
	 * @param pageRequest 페이지, 한 페이지에 들어가는 아이템 개수
	 * @return 매니저 리뷰 리스트
	 */
	@Override
	public List<ManagerReviewResponse> managersManagerReviews(@PathVariable long managerId, PageRequest pageRequest) {
		return managerService.findReviews(managerId, pageRequest);
	}
}
