package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({UserRepository.class})
@DataJpaTest
class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("계정 ID로 사용자 조회")
  void findByAccountIdTest() {
    //GIVEN
    String accountId = "testAccountId";
    String pw = "testPw123123!";
    String phoneNum = "010-1234-1234";
    UserType userType = UserType.TRAVELER;
    User user = new User(accountId, pw, phoneNum, userType);
    em.persist(user);
    em.flush();

    //WHEN
    Optional<User> existResult = userRepository.findByAccountId(accountId);
    Optional<User> notExistResult = userRepository.findByAccountId("new account id");

    //THEN
    //성공 케이스 - 1 - 존재하는 account id로 조회한 경우
    assertTrue(existResult.isPresent());

    //실패 케이스 - 1 - 존재하지 않는 account id로 조회한 경우
    assertTrue(notExistResult.isEmpty());
  }
}