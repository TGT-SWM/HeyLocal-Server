/**
 * packageName    : com.heylocal.traveler.repository
 * fileName       : PlaceItemRepository
 * author         : 신우진
 * date           : 2022/09/16
 * description    : 스케줄 내 장소 아이템에 대한 레포지터리
 */

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
