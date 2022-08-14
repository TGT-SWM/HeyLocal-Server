package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
