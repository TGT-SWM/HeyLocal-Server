package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.plan.list.PlaceItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@Import({PlaceItemRepository.class})
@DataJpaTest
class PlaceItemRepositoryTest {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PlaceItemRepository placeItemRepository;

	@Test
	@DisplayName("장소 아이템 삭제")
	void removeTest() {
		// GIVEN
		long placeItemId = 1L;
		PlaceItem placeItem = PlaceItem.builder()
				.itemIndex(0)
				.build();
		em.persist(placeItem);

		long notExistPlaceItemId = placeItemId + 1;
		PlaceItem notExistPlaceItem = PlaceItem.builder()
				.id(notExistPlaceItemId)
				.itemIndex(0)
				.build();

		// WHEN - THEN
		assertAll(
				// 성공 케이스 - 1 - 문제 없이 삭제 완료
				() -> assertDoesNotThrow(() -> placeItemRepository.remove(placeItem)),
				// 실패 케이스 - 1 - 존재하지 않는 아이템 삭제
				() -> assertThrows(IllegalArgumentException.class, () -> placeItemRepository.remove(notExistPlaceItem))
		);
	}
}