package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class TravelOnRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * 여행On을 저장하는 메서드
   * @param travelOn
   */
  public void addTravelOn(TravelOn travelOn) {
    em.persist(travelOn);
  }
}
