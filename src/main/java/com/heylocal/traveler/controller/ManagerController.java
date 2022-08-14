package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ManagersApi;
import com.heylocal.traveler.dto.Sample;
import org.springframework.http.ResponseEntity;

public class ManagerController implements ManagersApi {
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
	public ResponseEntity<Void> managersManagerIdProfileGet(long managerId, boolean simple) {
		return null;
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
