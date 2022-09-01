package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.heylocal.traveler.dto.TravelOnDto.TravelOnSortType;

@Repository
@RequiredArgsConstructor
public class TravelOnRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * 여행On을 저장하는 메서드
   * @param travelOn
   */
  public void saveTravelOn(TravelOn travelOn) {
    em.persist(travelOn);
  }

  /**
   * 여행 On 을 조회하는 메서드
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAll(int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 있는 여행 On 을 조회하는 메서드
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinion(int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " join fetch t.opinionList";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 조회하는 메서드
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinion(int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " left join fetch t.opinionList" +
        " where t.opinionList.size = 0";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAllByRegion(Region region, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.region = :region";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 있는 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinionByRegion(Region region, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " join fetch t.opinionList" +
        " where t.region = :region";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinionByRegion(Region region, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " left join fetch t.opinionList" +
        " where t.region = :region" +
        " and t.opinionList.size = 0";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAllByState(String state, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.region.state = :state";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 있는 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinionByState(String state, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " join fetch t.opinionList" +
        " where t.region.state = :state";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param firstIndex 첫 아이템의 index
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinionByState(String state, int firstIndex, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " left join fetch t.opinionList" +
        " where t.region.state = :state" +
        " and t.opinionList.size = 0";

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setFirstResult(firstIndex)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * order by 문을 쿼리에 붙이는 메서드
   * @param jpql 원본 jpql 쿼리
   * @param sortType 정렬 기준
   * @return
   */
  private String appendJpqlWithOrderBy(String jpql, TravelOnSortType sortType) {
    String sortCriterionName = "";

    switch (sortType) {
      case DATE:
        sortCriterionName = "createdDate";
        break;
      case VIEWS:
        sortCriterionName = "views";
        break;
      case OPINIONS:
        sortCriterionName = "opinionList.size";
        break;
    }

    return jpql + " order by t." + sortCriterionName + " desc";
  }
}
