package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class OpinionRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * Opinion 저장 메서드
   * @param opinion
   */
  public void save(Opinion opinion) {
    em.persist(opinion);
  }

  /**
   * id 로 Opinion 조회 메서드
   * @param id
   * @return
   */
  public Optional<Opinion> findById(long id) {
    Opinion opinion = em.find(Opinion.class, id);
    return Optional.ofNullable(opinion);
  }

  /**
   * 답변 ID 와 여행On ID 로 답변을 조회하는 메서드
   * @param opinionId
   * @param travelOnId
   * @return
   */
  public Optional<Opinion> findByIdAndTravelOn(long opinionId, long travelOnId) {
    Opinion opinion;
    String jpql = "select o from Opinion o" +
        " where o.id = :opinionId" +
        " and o.travelOn.id = :travelOnId";

    try {
      opinion = em.createQuery(jpql, Opinion.class)
          .setParameter("opinionId", opinionId)
          .setParameter("travelOnId", travelOnId)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(opinion);
  }

  /**
   * Opinion 을 삭제하는 메서드
   * @param opinion
   */
  public void remove(Opinion opinion) {
    em.remove(opinion);
  }
}
