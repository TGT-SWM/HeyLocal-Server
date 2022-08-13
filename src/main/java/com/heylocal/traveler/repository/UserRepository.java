package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final EntityManager em;

  public List<User> findByAccountId(String accountId) {
    String jpql = "select u from User u where u.accountId = :accountId";

    List<User> resultList = em.createQuery(jpql, User.class)
        .setParameter("accountId", accountId)
        .getResultList();

    return resultList;
  }
}
