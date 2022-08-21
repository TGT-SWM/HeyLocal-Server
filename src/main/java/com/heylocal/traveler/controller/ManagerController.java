package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.controller.exception.NotFoundException;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileResponse;
import com.heylocal.traveler.dto.ManagerDto.ManagerReviewResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.Sample;
import com.heylocal.traveler.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
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

	@Override
	public ManagerProfileResponse managersManagerIdProfileGet(long managerId, boolean simple) throws NotFoundException {
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

	// 매니저 리뷰 리스트 조회
	@GetMapping(value = "/{managerId}/reviews")
	public List<ManagerReviewResponse> managersManagerReviews(@PathVariable long managerId, PageRequest pageRequest) {
		return managerService.findReviews(managerId, pageRequest);
	}
}
