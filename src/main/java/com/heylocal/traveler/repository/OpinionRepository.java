package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
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
   * <pre>
   * 장소 Id 로 조회하는 메서드
   * 페이징 처리까지 수행한다.
   * </pre>
   * @param placeId 장소 Id
   * @param lastItemId 클라이언트가 받은 마지막 답변 Id (null인 경우, 처음부터 조회)
   * @param size 한 페이지당 표시할 답변 개수
   * @return
   */
  public List<Opinion> findByPlaceId(long placeId, @Nullable Long lastItemId, int size) {
    String jpql = "select o from Opinion o" +
        " where o.id < :lastItemId" +
        " and o.place.id = :placeId" +
        " order by o.id desc";

    TypedQuery<Opinion> typedQuery = em.createQuery(jpql, Opinion.class)
        .setParameter("placeId", placeId);

    if (lastItemId == null) {
      typedQuery.setParameter("lastItemId", Long.MAX_VALUE);
    } else {
      typedQuery.setParameter("lastItemId", lastItemId);
    }

    return typedQuery.setMaxResults(size).getResultList();
  }

  /**
   * <pre>
   * 특정 사용자 ID 로 답변을 조회하는 메서드
   * Id 내림차순으로 페이징하여 조회한다.
   * </pre>
   * @param userId 사용자 Id
   * @param lastItemId 마지막으로 받은 답변 Id
   * @param size 페이지 당 아이템 개수
   * @return
   */
  public List<Opinion> findByUserIdOrderByIdDesc(long userId, long lastItemId, int size) {
    String jpql = "select o from Opinion o" +
        " where o.author.id = :userId" +
        " and o.id < :lastItemId";

    List<Opinion> resultList = em.createQuery(jpql, Opinion.class)
        .setParameter("userId", userId)
        .setParameter("lastItemId", lastItemId)
        .setMaxResults(size)
        .getResultList();

    return resultList;
  }

  /**
   * Opinion 을 삭제하는 메서드
   * @param opinion
   */
  public void remove(Opinion opinion) {
    em.remove(opinion);
  }
}
