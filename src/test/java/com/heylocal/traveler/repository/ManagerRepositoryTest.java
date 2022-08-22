package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({ ManagerRepository.class })
class ManagerRepositoryTest {
	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("매니저 조회")
	void findOneTest() {
		// GIVEN
		Manager manager = new Manager("김현지", null, null, null);
		em.persist(manager);

		long id = manager.getId();
		long notExistsId = id + 1;

		// WHEN
		Optional<Manager> optResult = managerRepository.findOne(id);
		Optional<Manager> optNotExistsResult = managerRepository.findOne(notExistsId);

		// THEN
		// 성공 케이스 - 1 - 매니저 조회 결과가 존재하는 경우
		Assertions.assertTrue(optResult.isPresent());
		// 성공 케이스 - 2 - 매니저 조회 결과가 저장한 것과 일치하는 경우
		assertThat(optResult.get()).isEqualTo(manager);

		// 실패 케이스 - 1 - 존재하지 않는 매니저를 조회하는 경우
		Assertions.assertTrue(optNotExistsResult.isEmpty());
	}
}