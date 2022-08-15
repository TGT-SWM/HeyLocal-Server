package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.userreview.ManagerReview;
import com.heylocal.traveler.dto.ManagerDto.ManagerReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerReviewRepository {
	private final EntityManager em;

	public List<ManagerReview> findAll(ManagerReviewRequest request) {
		String jpql = "select r from ManagerReview r where r.writer.id = :id order by r.createdDate desc";

		int page = request.getPage();
		int pageSize = request.getPageSize();
		int firstResult = (page - 1) * pageSize;

		return em.createQuery(jpql, ManagerReview.class)
				.setParameter("id", request.getManagerId())
				.setFirstResult(firstResult)
				.setMaxResults(pageSize)
				.getResultList();
	}
}
