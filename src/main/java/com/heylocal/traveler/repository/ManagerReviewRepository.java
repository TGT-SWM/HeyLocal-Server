package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.userreview.ManagerReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerReviewRepository {
	private final EntityManager em;

	public List<ManagerReview> findByManagerId(Long id) {
		String jpql = "select r from ManagerReview r where r.writer.id = :id";

		return em.createQuery(jpql, ManagerReview.class)
				.setParameter("id", id)
				.getResultList();
	}
}
