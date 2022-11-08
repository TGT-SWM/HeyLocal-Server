/**
 * packageName    : com.heylocal.traveler.repository
 * fileName       : UserProfileRepository
 * author         : 우태균
 * date           : 2022/08/24
 * description    : 사용자 프로필에 대한 레포지터리
 */

package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.UserProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserProfileRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * 사용자 프로필을 저장하는 메서드
   * @param userProfile 저장할 사용자 프로필
   * @return
   */
  public UserProfile saveUserProfile(UserProfile userProfile) {
    em.persist(userProfile);
    return userProfile;
  }

  /**
   * 사용자 프로필을 노하우 순으로 조회하는 메서드
   * @param size 조회할 아이템 개수
   * @return
   */
  public List<UserProfile> findSortedByKnowHowDesc(int size) {
    String jpql = "select u from UserProfile u" +
        " order by u.knowHow desc, u.user.opinionList.size desc, u.id";
    List<UserProfile> resultList = em.createQuery(jpql, UserProfile.class)
        .setMaxResults(size)
        .getResultList();
    return resultList;
  }
}
