package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import({TravelerProfileRepository.class})
@DataJpaTest
class TravelerProfileRepositoryTest {
  @Autowired
  private TravelerProfileRepository travelerProfileRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("사용자(여행자)프로필 저장")
  void saveTravelerProfileTest() {
    //GIVEN
    int possessionPoint = 10000;
    Traveler traveler = Traveler.builder()
        .accountId("accountId")
        .password("password123!")
        .nickname("nickname")
        .phoneNumber("010-1111-1111")
        .userType(UserType.TRAVELER)
        .build();
    em.persist(traveler);

    //WHEN

    //THEN
    //성공 케이스 - 1
    assertDoesNotThrow(() -> travelerProfileRepository.saveTravelerProfile(traveler, possessionPoint, null));
  }

}