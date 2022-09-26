package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class OpinionImageContentRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * OpinionImageContent 엔티티를 저장하는 메서드
   * @param entity
   */
  public void save(OpinionImageContent entity) {
    em.persist(entity);
  }

  /**
   * id 로 OpinionImageContent 엔티티를 찾는 메서드
   * @param id
   * @return
   */
  public Optional<OpinionImageContent> findById(long id) {
    OpinionImageContent entity = em.find(OpinionImageContent.class, id);
    return Optional.ofNullable(entity);
  }

  /**
   * objectKeyName 필드로 OpinionImageContent 엔티티를 찾는 메서드
   * @param objectKeyName
   * @return
   */
  public Optional<OpinionImageContent> findByObjectKeyName(String objectKeyName) {
    String jpql = "select o from OpinionImageContent o" +
        " where o.objectKeyName = :objectKeyName";
    OpinionImageContent entity;

    try {
      entity = em.createQuery(jpql, OpinionImageContent.class)
          .setParameter("objectKeyName", objectKeyName)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.ofNullable(entity);
  }

  /**
   * 해당 엔티티를 제거하는 메서드
   * @param entity
   */
  public void remove(OpinionImageContent entity) {
    em.remove(entity);
  }
}
