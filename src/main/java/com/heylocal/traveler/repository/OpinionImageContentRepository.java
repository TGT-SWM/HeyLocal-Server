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

  public void save(OpinionImageContent entity) {
    em.persist(entity);
  }

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
}
