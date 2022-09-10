package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OpinionRepository {
  @PersistenceContext
  private EntityManager em;

  public void save(Opinion opinion) {
    em.persist(opinion);
  }
}
