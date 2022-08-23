package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final EntityManager em;

  /**
   * 사용자를 저장하는 메서드
   * @param accountId
   * @param encodedPw
   * @param nickname
   * @return
   */
  public User saveUser(String accountId, String encodedPw, String nickname, UserRole role) {
    User traveler = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .userRole(role)
        .nickname(nickname)
        .build();

    em.persist(traveler);

    return traveler;
  }

  public Optional<User> findById(long userId) {
    User user;

    user = em.find(User.class, userId);

    return Optional.ofNullable(user);
  }

  /**
   * 계정id로 사용자 찾기
   * @param accountId
   * @return
   */
  public Optional<User> findByAccountId(String accountId) {
    String jpql = "select u from User u where u.accountId = :accountId";
    User user;

    try {
      user = em.createQuery(jpql, User.class)
          .setParameter("accountId", accountId)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return Optional.empty();
    }

    return Optional.of(user);
  }

}
