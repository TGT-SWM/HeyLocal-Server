package com.heylocal.traveler.repository;

import com.heylocal.traveler.dto.PageDto.PageRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({ ManagerReviewRepository.class })
class ManagerReviewRepositoryTest {
	@Autowired
	private ManagerReviewRepository managerReviewRepository;

	@Test
	@DisplayName("매니저 리뷰 조회")
	void findByManagerIdTest() {
		// GIVEN
		// 매니저 리뷰 등록하도록 수정
		long managerId = 1L;
		int page = 1;
		int pageSize = 10;
		PageRequest pageRequest = new PageRequest(page, pageSize);

		// WHEN & THEN
		Assertions.assertDoesNotThrow(
				() -> managerReviewRepository.findAll(managerId, pageRequest)
		);
	}
}