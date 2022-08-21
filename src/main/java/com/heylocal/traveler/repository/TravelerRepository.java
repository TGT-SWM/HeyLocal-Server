package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TravelerRepository {
  private final EntityManager em;

  /**
   * Traveler 저장 메서드
   * @param accountId 계정 id
   * @param encodedPw 암호화된 비밀번호
   * @param nickname 닉네임
   * @param phoneNumber 휴대폰 번호
   * @return 저장된 Traveler 엔티티
   */
  public Traveler saveTraveler(String accountId, String encodedPw, String nickname, String phoneNumber) {
    Traveler traveler = Traveler.builder()
        .accountId(accountId)
        .password(encodedPw)
        .phoneNumber(phoneNumber)
        .userType(UserType.TRAVELER)
        .nickname(nickname)
        .build();
    em.persist(traveler);

    return traveler;
  }

  /**
   * id(pk)로 Traveler를 찾는 메서드
   * @param id pk값
   * @return 찾은 Traveler 엔티티 (Optional로 Wrapping 됨)
   */
  public Optional<Traveler> findById(long id) {
    Traveler traveler = em.find(Traveler.class, id);
    return Optional.ofNullable(traveler);
  }

  /**
   * 계정 ID로 Traveler를 찾는 메서드
   * @param accountId 계정 id
   * @return 찾은 Traveler 엔티티 (Optional로 Wrapping 됨)
   */
  public Optional<Traveler> findByAccountId(String accountId) {
    String jpql = "select t from Traveler t" +
        " where t.accountId = :accountId";
    Traveler traveler;

    try {
      traveler = em.createQuery(jpql, Traveler.class)
          .setParameter("accountId", accountId)
          .getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    return Optional.of(traveler);
  }
}
