package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({PlaceRepository.class})
@DataJpaTest
class PlaceRepositoryTest {
  @PersistenceContext
  private EntityManager em;
  @Autowired
  private PlaceRepository placeRepository;

  @Test
  @DisplayName("장소 저장")
  void saveTest() {
    //GIVEN
    long placeId = 1L;
    Place notPersistPlace = getNotPersistPlace(placeId);

    //WHEN
    placeRepository.save(notPersistPlace);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장한 Place 조회
        () -> assertSame(notPersistPlace, em.find(Place.class, placeId)),
        //성공 케이스 - 2 - Flush
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("id 로 장소 조회")
  void findBuyIdTest() {
    //GIVEN
    long placeId = 1L;
    Place place = getPersistPlace(placeId);

    //WHEN
    Optional<Place> result = placeRepository.findById(placeId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회가 되었는지
        () -> assertTrue(result.isPresent()),
        //성공 케이스 - 2 - 조회된 엔티티가 같은 것인지
        () -> assertSame(place, result.get())
    );
  }

  /**
   * 영속화되지 않은 새 Place 엔티티를 반환하는 메서드
   * @param id 새 Place 엔티티의 ID
   * @return
   */
  private Place getNotPersistPlace(long id) {
    PlaceCategory category = PlaceCategory.AD5;
    String name = "myPlace";
    String roadAddress = "myRoadAddress";
    String address = "myAddress";
    double lat = 10;
    double lng = 10;
    String link = "myLink";
    Region region = Region.builder()
        .state("myState")
        .city("myCity")
        .build();

    em.persist(region);

    return Place.builder()
        .id(id)
        .category(category)
        .name(name)
        .roadAddress(roadAddress)
        .address(address)
        .lat(lat)
        .lng(lng)
        .link(link)
        .region(region)
        .build();
  }

  /**
   * 영속화된 새 Place 엔티티를 반환하는 메서드
   * @param id 새 Place 엔티티의 ID
   * @return
   */
  private Place getPersistPlace(long id) {
    Place place = getNotPersistPlace(id);
    em.persist(place);
    return place;
  }
}