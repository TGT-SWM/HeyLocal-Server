package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RegionRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * <pre>
   * 특별시, 광역시, 제주도, 그 외 시·군 을 조회하는 메서드
   * </pre>
   * @return
   */
  public List<Region> findAll() {
    String jpql = "select r from Region r" +
        " where (r.state not in ('서울특별시', '부산광역시', '대구광역시', '인천광역시', '광주광역시', '대전광역시', '울산광역시', '세종특별자치시', '제주특별자치도')" +
          " and r.city not like '%구')" +
        " or (r.state in ('서울특별시', '부산광역시', '대구광역시', '인천광역시', '광주광역시', '대전광역시', '울산광역시', '세종특별자치시', '제주특별자치도')" +
          " and r.city is null)";

    List<Region> result = em.createQuery(jpql, Region.class).getResultList();

    return result;
  }

  /**
   * id 로 Region 엔티티를 조회하는 메서드
   * @param id
   * @return
   */
  public Optional<Region> findById(long id) {
    Region region = em.find(Region.class, id);
    return Optional.ofNullable(region);
  }

  /**
   * State 와 City 로 Region 엔티티를 조회하는 메서드
   * @param state
   * @param city
   * @return
   */
  public Optional<Region> findByStateAndCity(String state, String city) {
    Region result;
    String jpql = "select r from Region r" +
        " where r.state = :state" +
        " and r.city = :city";

    try {
      result = em.createQuery(jpql, Region.class)
          .setParameter("state", state)
          .setParameter("city", city)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(result);
  }

  /**
   * state 만으로 Region 을 조회하는 메서드
   * @param state
   * @return
   */
  public List<Region> findByState(String state) {
    List<Region> result;
    String jpql = "select r from Region r" +
        " where r.state = :state";

    result = em.createQuery(jpql, Region.class)
        .setParameter("state", state)
        .getResultList();

    return result;
  }

  public Optional<Region> findByStateKeyword(String keyword) {
    Region region;
    String jpql = "select r from Region r" +
        " where r.state like :state" +
        " and r.city is null";

    keyword = "%" + keyword + "%";
    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("state", keyword)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(region);
  }

  public Optional<Region> findByCityKeyword(String keyword) {
    Region region;
    String jpql = "select r from Region r" +
        " where r.city like :city";

    keyword = "%" + keyword + "%";
    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("city", keyword)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(region);
  }

  /**
   * city 가 null 이고 해당 state를 갖는 Region 을 조회하는 메서드
   * @param state
   * @return
   */
  public Optional<Region> findByStateAndCityIsNull(String state) {
    Region region;
    String jpql = "select r from Region r" +
        " where r.state = :state" +
        " and r.city is null";

    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("state", state)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(region);
  }
}
