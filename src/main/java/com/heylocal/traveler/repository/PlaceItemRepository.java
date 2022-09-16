package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.plan.list.PlaceItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PlaceItemRepository {
	@PersistenceContext
	private EntityManager em;

	/**
	 * <pre>
	 * PlaceItem을 삭제합니다.
	 * @param placeItem PlaceItem 엔티티
	 * </pre>
	 */
	public void remove(PlaceItem placeItem) {
		em.remove(placeItem);
	}
}
