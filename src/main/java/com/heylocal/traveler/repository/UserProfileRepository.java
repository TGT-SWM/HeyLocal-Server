package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserProfileRepository {
  private final EntityManager em;

  /**
   * 사용자 프로필을 저장하는 메서드
   * @param userId
   * @param knowHow
   * @param imageUrl
   * @return
   */
  public UserProfile saveUserProfile(long userId, int knowHow, @Nullable String imageUrl) {
    UserProfile travelerProfile;
    User user;

    user = em.find(User.class, userId);
    travelerProfile = UserProfile.builder()
        .user(user)
        .knowHow(knowHow)
        .imageUrl(imageUrl)
        .build();
    em.persist(travelerProfile);

    return travelerProfile;
  }
}
