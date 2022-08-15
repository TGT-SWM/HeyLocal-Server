package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Manager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

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

		// WHEN
		Manager foundManager = managerRepository.findOne(manager.getId());

		// THEN
		assertThat(foundManager).isEqualTo(manager);
	}
}