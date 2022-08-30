package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RegionRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * State 와 City 로 Region 엔티티를 조회하는 메서드
   * @param state
   * @param city
   * @return
   */
  public Region findByStateAndCity(String state, String city) {
    String jpql = "select r from Region r" +
        " where r.state = :state" +
        " and r.city = :city";

    Region result = em.createQuery(jpql, Region.class)
        .setParameter("state", state)
        .setParameter("city", city)
        .getSingleResult();

    return result;
  }
}
