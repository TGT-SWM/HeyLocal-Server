package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.controller.NotFoundException;
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
		ManagerProfileResponse response = managerService.findProfileById(managerId);
		if (response == null) {
			throw new NotFoundException(NotFoundCode.NO_INFO);
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
