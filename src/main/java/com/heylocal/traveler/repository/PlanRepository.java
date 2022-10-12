/**
 * packageName    : com.heylocal.traveler.repository
 * fileName       : PlanRepository
 * author         : 신우진
 * date           : 2022/08/29
 * description    : 플랜에 대한 레포지터리
 */

package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.plan.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRepository {
	private final EntityManager em;

	/**
	 * <pre>
	 * 플랜을 저장합니다.
	 * @param plan 저장할 플랜 엔티티
	 * @return 저장한 플랜 엔티티
	 * </pre>
	 */
	public Plan save(Plan plan) {
		em.persist(plan);
		return plan;
	}

	/**
	 * <pre>
	 * 플랜을 삭제합니다.
	 * @param plan 플랜 엔티티
	 * </pre>
	 */
	public void remove(Plan plan) {
		em.remove(plan);
	}

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

	/**
	 * <pre>
	 * ID를 통해 플랜을 조회합니다.
	 * @param planId 플랜 ID
	 * @return Plan의 Optional
	 * </pre>
	 */
	public Optional<Plan> findById(long planId) {
		Plan plan = em.find(Plan.class, planId);
		return Optional.ofNullable(plan);
	}
}
