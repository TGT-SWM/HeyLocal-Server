package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.controller.exception.NotFoundException;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileResponse;
import com.heylocal.traveler.dto.Sample;
import com.heylocal.traveler.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ManagerController implements ManagersApi {
	private final ManagerService managerService;

	/**
	 * @param managerId
	 * @param page
	 * @return
	 */
	@Override
	public ResponseEntity<Void> managersManagerIdNotesGet(long managerId, int page) {
		return null;
	}

	/**
	 * @param managerId
	 * @param noteId
	 * @return
	 */
	@Override
	public ResponseEntity<Void> managersManagerIdNotesNoteIdGet(long managerId, long noteId) {
		return null;
	}

	/**
	 * @param managerId
	 * @param simple
	 * @return
	 */
	@Override
	public ManagerProfileResponse managersManagerIdProfileGet(long managerId, boolean simple) throws NotFoundException {
		ManagerProfileResponse response = managerService.findByUserId(managerId);
		if (response == null) {
			throw new NotFoundException("정보가 존재하지 않습니다.");
		}

		return response;
	}

	/**
	 * @param managerId
	 * @param body
	 * @return
	 */
	@Override
	public ResponseEntity<Void> managersManagerIdReviewsPost(long managerId, Sample body) {
		return null;
	}
}
