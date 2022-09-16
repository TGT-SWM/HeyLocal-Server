package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.plan.list.PlaceItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PlaceItemRepository {
	@PersistenceContext
	private EntityManager em;

	public void remove(PlaceItem placeItem) {
		em.remove(placeItem);
	}
}
