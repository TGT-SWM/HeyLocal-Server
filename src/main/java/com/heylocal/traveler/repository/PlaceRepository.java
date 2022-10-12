/**
 * packageName    : com.heylocal.traveler.repository
 * fileName       : PlaceRepository
 * author         : 우태균
 * date           : 2022/09/08
 * description    : 장소에 대한 레포지터리
 */

package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.place.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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

  /**
   * 연관된 Opinion 개수가 많은 순으로 조회하는 메서드
   * @param size 조회할 Place 개수
   * @return
   */
  public List<Place> findPlaceOrderByOpinionSizeDesc(int size) {
    String jpql = "select p from Place p" +
        " order by p.opinionList.size desc";

    List<Place> resultList = em.createQuery(jpql, Place.class)
        .setMaxResults(size)
        .getResultList();

    return resultList;
  }
}
