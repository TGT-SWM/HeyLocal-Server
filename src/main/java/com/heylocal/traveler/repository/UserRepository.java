package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final EntityManager em;

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
