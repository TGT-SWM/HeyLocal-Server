package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RegionRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * id 로 Region 엔티티를 조회하는 메서드
   * @param id
   * @return
   */
  public Optional<Region> findById(long id) {
    Region region = em.find(Region.class, id);
    return Optional.ofNullable(region);
  }

  /**
   * State 와 City 로 Region 엔티티를 조회하는 메서드
   * @param state
   * @param city
   * @return
   */
  public Optional<Region> findByStateAndCity(String state, String city) {
    Region result;
    String jpql = "select r from Region r" +
        " where r.state = :state" +
        " and r.city = :city";

    try {
      result = em.createQuery(jpql, Region.class)
          .setParameter("state", state)
          .setParameter("city", city)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(result);
  }

  /**
   * state 만으로 Region 을 조회하는 메서드
   * @param state
   * @return
   */
  public List<Region> findByState(String state) {
    List<Region> result;
    String jpql = "select r from Region r" +
        " where r.state = :state";

    result = em.createQuery(jpql, Region.class)
        .setParameter("state", state)
        .getResultList();

    return result;
  }

  public Optional<Region> findByStateKeyword(String keyword) {
    Region region;
    String jpql = "select r from Region r" +
        " where r.state like :state" +
        " and r.city is null";

    keyword = "%" + keyword + "%";
    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("state", keyword)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(region);
  }

  public Optional<Region> findByCityKeyword(String keyword) {
    Region region;
    String jpql = "select r from Region r" +
        " where r.city like :city";

    keyword = "%" + keyword + "%";
    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("city", keyword)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(region);
  }
}
