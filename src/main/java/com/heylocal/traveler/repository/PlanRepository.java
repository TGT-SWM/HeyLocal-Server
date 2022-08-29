package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.plan.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlanRepository {
	private final EntityManager em;

	/**
	 * <pre>
	 * 사용자가 작성한 스케줄을 리스트로 반환합니다.
	 * @param userId 사용자의 아이디
	 * @return 스케줄 도메인의 리스트
	 * </pre>
	 */
	public List<Plan> findByUserId(long userId) {
		String jpql = "select t from Plan t where t.user.id = :id";

		return em.createQuery(jpql, Plan.class)
				.setParameter("id", userId)
				.getResultList();
	}
}
