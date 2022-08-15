package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.controller.exception.NotFoundException;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileResponse;
import com.heylocal.traveler.dto.Sample;
import com.heylocal.traveler.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
		ManagerProfileResponse response;

		// 매니저 프로필 조회
		// simple 파라미터 값에 따라 관련 서비스 호출
		// simple 쿼리 파라미터로 구분하는 대신 경로와 컨트롤러로 구분하는게 어떨지?
		if (simple) {
			response = (ManagerProfileResponse) managerService.findSimpleProfileById(managerId);
		} else {
			response = managerService.findProfileById(managerId);
		}

		// 매니저 프로필 정보가 없는 경우
		if (response == null) {
			throw new NotFoundException("정보가 존재하지 않습니다.");
		}

		return response;
	}

	@Override
	public ResponseEntity<Void> managersManagerIdReviewsPost(long managerId, Sample body) {
		return null;
	}
}
