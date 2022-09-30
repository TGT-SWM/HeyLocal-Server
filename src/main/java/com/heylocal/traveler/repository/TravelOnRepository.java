package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAll(Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " WHERE " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행 On을 사용자 ID로 조회하는 메서드
   * @param userId 사용자 ID
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 ID (Nullable)
   * @param size 한 페이지의 아이템 개수
   * @param sortType 정렬 기준
   * @return 여행 On의 리스트
   */
  public List<TravelOn> findAllByUserId(long userId, Long lastItemId, int size, TravelOnSortType sortType) {
    String jpql = "select t from TravelOn t" +
            " where t.author.id = :userId"
            + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    return em.createQuery(jpql, TravelOn.class)
            .setParameter("userId", userId)
            .setMaxResults(size)
            .getResultList();
  }

  /**
   * 여행On 을 id 로 찾는 메서드
   * @param id 찾을 id
   * @return
   */
  public Optional<TravelOn> findById(long id) {
    return Optional.ofNullable(em.find(TravelOn.class, id));
  }

  /**
   * 답변이 있는 여행 On 을 조회하는 메서드
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinion(Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.opinionList.size > 0" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 조회하는 메서드
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinion(Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.opinionList.size = 0" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAllByRegion(Region region, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.region = :region" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 있는 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinionByRegion(Region region, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where (t.region = :region" +
        " and t.opinionList.size > 0)" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 Region 으로 조회하는 메서드
   * @param region 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinionByRegion(Region region, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where (t.region = :region" +
        " and t.opinionList.size = 0)" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("region", region)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findAllByState(String state, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where t.region.state = :state" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 있는 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findHasOpinionByState(String state, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where (t.region.state = :state" +
        " and t.opinionList.size > 0)" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 답변이 없는 여행 On 을 Region.state 로 조회하는 메서드
   * @param state 관련 지역
   * @param lastItemId 클라이언트가 마지막으로 받은 아이템의 id(pk)
   * @param size 페이지당 아이템 개수
   * @param sortType 정렬기준
   * @return
   */
  public List<TravelOn> findNoOpinionByState(String state, Long lastItemId, int size, TravelOnSortType sortType) {
    List<TravelOn> result;
    String jpql = "select t from TravelOn t" +
        " where (t.region.state = :state" +
        " and t.opinionList.size = 0)" +
        " and " + getPaginationCondition("t", sortType, lastItemId);

    jpql = appendJpqlWithOrderBy(jpql, sortType);
    result = em.createQuery(jpql, TravelOn.class)
        .setParameter("state", state)
        .setMaxResults(size)
        .getResultList();

    return result;
  }

  /**
   * 여행On를 삭제하는 메서드
   * @param target
   */
  public void remove(TravelOn target) {
    em.remove(target);
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

    return jpql + " order by t." + sortCriterionName + " desc, t.id desc";
  }

  /**
   * <pre>
   * Cursor 기반 페이지네이션을 하기 위한 조건절 생성
   *
   * [최신순 정렬인 경우]
   * 최신순 정렬은 단순하게 id 컬럼을 커서로 삼아 페이지네이션을 할 수 있음.
   * `WHERE id < 마지막_아이템의_id` 와 같이 조회하면 됨.
   * 왜냐하면 id 자체가 유니크한 값을 갖기 때문임.
   *
   * [조회수순, 답변개수순 정렬인 경우]
   * 조회수나 답변개수는 유니크한 값이 아님. 따라서 최신순 정렬처럼 단순하게 페이지네이션을 할 수 없음.
   * `WHERE 조회수 < 마지막_아이템의_조회수` 와 같은 조건으로 페이징하면, 같은 조회수를 갖는 아이템이 누락됨.
   * `WHERE 조회수 <= 마지막_아이템의_조회수` 와 같은 조건으로 페이징하면, 같은 조회수를 갖는 아이템이 중복 조회됨.
   * 따라서 '유니크하지 않는 컬럼(조회수, 답변수)'과 '유니크한 컬럼(id)' 을 함께 사용하여 조회해야함.
   *
   * <a href="https://velog.io/@minsangk/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Cursor-based-Pagination-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0">관련 Reference</a>
   * </pre>
   * @param travelOnAlias TravelOn 별칭
   * @param sortType 정렬 기준
   * @param lastItemId 마지막 아이템의 id(pk), `null` 이라면 처음부터 조회
   * @return 정렬 기준에 맞는 페이지네이션 조건절
   */
  private String getPaginationCondition(String travelOnAlias, TravelOnSortType sortType, Long lastItemId) {
    StringBuilder sb = new StringBuilder("");
    long id;
    int views;
    int opinionCount;

    //첫 페이지인 경우(lastItemId == null), 아닌 경우 구분하여 변수 초기화
    if (lastItemId == null) {
      id = Long.MAX_VALUE;
      views = Integer.MAX_VALUE;
      opinionCount = Integer.MAX_VALUE;
    } else {
      TravelOn lastItem = em.find(TravelOn.class, lastItemId);
      id = lastItem.getId();
      views = lastItem.getViews();
      opinionCount = lastItem.getOpinionList().size();
    }

    //정렬 종류에 따라, 페이지네이션 조건 만들기
    switch (sortType) {
      case DATE:
        sb.append(travelOnAlias).append(".id < ").append(id);
        break;

      case VIEWS: // (t.views < :views) OR (t.views = :views AND t.id < :id)
        sb.append("(" + travelOnAlias + ".views < " + views + ")")
            .append(" or ")
            .append("(" + travelOnAlias + ".views = " + views)
              .append(" and " + travelOnAlias + ".id < " + id)
            .append(")");
        break;

      case OPINIONS: // (t.opinionList.size < :size) OR (t.opinionList.size = :size AND t.id < :id)
        sb.append("(" + travelOnAlias + ".opinionList.size < " + opinionCount + ")")
            .append(" or ")
            .append("(" + travelOnAlias + ".opinionList.size = " + opinionCount)
              .append(" and " + travelOnAlias + ".id < " + id)
            .append(")");
        break;
    }

    return sb.toString();
  }
}
