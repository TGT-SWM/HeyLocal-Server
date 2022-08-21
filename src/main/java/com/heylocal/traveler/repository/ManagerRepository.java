package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ManagerRepository {
	private final EntityManager em;

	public Manager findOne(long id) {
		return em.find(Manager.class, id);
	}
}
