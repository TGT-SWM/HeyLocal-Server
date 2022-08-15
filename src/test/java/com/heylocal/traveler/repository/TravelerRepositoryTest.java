package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({TravelerRepository.class})
@DataJpaTest
class TravelerRepositoryTest {
  @Autowired
  private TravelerRepository travelerRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("Traveler 저장")
  void saveTraveler() {
    //GIVEN
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    String phoneNumber = "010-1111-1111";

    //WHEN - THEN
    //성공 케이스 - 1
    assertDoesNotThrow(() -> travelerRepository.saveTraveler(accountId, encodedPw, nickname, phoneNumber));
  }

  @Test
  @DisplayName("Account Id 로 찾기")
  void findByAccountIdTest() {
    //GIVEN
    String existAccountId = "testAccountId1";
    String notExistAccountId = "testAccountId2";
    Traveler traveler = Traveler.builder()
        .accountId(existAccountId)
        .password("testPassword123!")
        .phoneNumber("010-1234-1234")
        .nickname("testNickname")
        .userType(UserType.TRAVELER)
        .build();

    em.persist(traveler);

    //WHEN
    Optional<Traveler> existResult = travelerRepository.findByAccountId(existAccountId);
    Optional<Traveler> notExistResult = travelerRepository.findByAccountId(notExistAccountId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 계정 ID로 찾는 경우
        () -> assertTrue(existResult.isPresent()),
        //실패 케이스 - 1 - 없는 계정 ID로 찾는 경우
        () -> assertTrue(notExistResult.isEmpty())
    );
  }
}