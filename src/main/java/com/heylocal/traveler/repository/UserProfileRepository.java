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
}
