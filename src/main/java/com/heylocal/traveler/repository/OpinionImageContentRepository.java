package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OpinionImageContentRepository {
  @PersistenceContext
  private EntityManager em;

  public void save(OpinionImageContent entity) {
    em.persist(entity);
  }
}
