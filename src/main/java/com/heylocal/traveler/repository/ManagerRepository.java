package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManagerRepository {
	private final EntityManager em;

	/**
	 * <pre>
	 * 매니저 ID를 통해 조회
	 * </pre>
	 * @param id 조회하고자 하는 매니저의 ID
	 * @return 매니저의 Optional 객체
	 */
	public Optional<Manager> findOne(long id) {
		Manager manager = em.find(Manager.class, id);
		return Optional.ofNullable(manager);
	}
}
