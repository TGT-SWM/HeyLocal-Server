package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.userreview.ManagerReview;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerReviewRepository {
	private final EntityManager em;

	/**
	 * <pre>
	 * 매니저 리뷰 리스트 조회
	 * </pre>
	 * @param managerId 조회하고자 하는 매니저의 ID(PK)
	 * @param pageRequest 페이지, 한 페이지의 크기
	 * @return 매니저 리뷰 리스트
	 */
	public List<ManagerReview> findAll(long managerId, PageRequest pageRequest) {
		String jpql = "select r from ManagerReview r where r.travel.manager.id = :id order by r.createdDate desc";

		int page = pageRequest.getPage();
		int pageSize = pageRequest.getPageSize();
		int firstResult = (page - 1) * pageSize;

		return em.createQuery(jpql, ManagerReview.class)
				.setParameter("id", managerId)
				.setFirstResult(firstResult)
				.setMaxResults(pageSize)
				.getResultList();
	}
}
