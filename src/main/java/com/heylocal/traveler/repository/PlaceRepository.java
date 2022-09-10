package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.place.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class PlaceRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * 장소 저장 메서드
   * @param place 저장할 장소 엔티티
   */
  public void save(Place place) {
    em.persist(place);
  }

  /**
   * id 로 장소 조회하는 메서드
   * @param id 조회할 id
   * @return
   */
  public Optional<Place> findById(long id) {
    return Optional.ofNullable(em.find(Place.class, id));
  }
}
