package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.ManagerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManagerProfileRepository {
	private final EntityManager em;

	/**
	 * <pre>
	 * 매니저 ID를 통해 프로필 조회
	 * </pre>
	 * @param userId 조회하고자 하는 매니저의 ID(PK)
	 * @return 매니저 프로필의 Optional 객체
	 */
	public Optional<ManagerProfile> findByUserId(long userId) {
		String jpql = "select m from ManagerProfile m where m.user.id = :id";
		ManagerProfile profile;

		try {
			profile = em.createQuery(jpql, ManagerProfile.class)
					.setParameter("id", userId)
					.getSingleResult();
		} catch (NoResultException e) {
			return Optional.empty();
		}

		return Optional.ofNullable(profile);
	}
}
