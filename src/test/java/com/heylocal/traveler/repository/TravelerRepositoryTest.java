package com.heylocal.traveler.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import({TravelerRepository.class})
@DataJpaTest
class TravelerRepositoryTest {
  @Autowired
  private TravelerRepository travelerRepository;

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
}