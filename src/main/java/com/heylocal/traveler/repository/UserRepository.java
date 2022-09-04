package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserRepository {
  @PersistenceContext
  private EntityManager em;

  /**
   * 사용자를 저장하는 메서드
   * @param user 저장할 사용자 엔티티
   * @return
   */
  public User saveUser(User user) {
    em.persist(user);
    return user;
  }

  /**
   * id(pk)로 사용자를 찾는 메서드
   * @param userId
   * @return
   */
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
