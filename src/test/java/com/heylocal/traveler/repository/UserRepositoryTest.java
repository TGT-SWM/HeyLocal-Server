package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import({UserRepository.class})
@DataJpaTest
class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("사용자 저장")
  void saveUserTest() {
    //GIVEN
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    User user = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .build();

    //WHEN - THEN
    assertAll(
        //성공 케이스 - 1 - 예외없이 저장되는지 확인
        () -> assertDoesNotThrow(() -> userRepository.saveUser(user)),
        //성공 케이스 - 2 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("pk로 User 찾기")
  void findByIdTest() {
    //GIVEN
    long userId;
    long notExistUserId;
    String accountId = "testAccountId";
    String encodedPw = "$2a$10$Cb/jltJ1KJkcWiylzKrOOuX/9R4r15QJ5V9snp6yfXqr2wB06WdHS";
    String nickname = "testNickname";
    User user = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .nickname(nickname)
        .userRole(UserRole.TRAVELER)
        .build();

    em.persist(user);
    userId = user.getId();
    notExistUserId = userId + 1;

    //WHEN
    Optional<User> succeedResult = userRepository.findById(userId);
    Optional<User> failResult = userRepository.findById(notExistUserId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush()),
        //성공 케이스 - 2 - 존재하는 id로 조회하는 경우
        () -> assertTrue(succeedResult.isPresent()),
        //실패 케이스 - 1 - 존재하지 않는 id로 조회하는 경우
        () -> assertFalse(failResult.isPresent())
    );
  }

  @Test
  @DisplayName("Account Id 로 User 찾기")
  void findByAccountIdTest() {
    //GIVEN
    String existAccountId = "testAccountId1";
    String notExistAccountId = "testAccountId2";
    User user = User.builder()
        .accountId(existAccountId)
        .password("testPassword123!")
        .nickname("testNickname")
        .userRole(UserRole.TRAVELER)
        .build();

    em.persist(user);

    //WHEN
    Optional<User> existResult = userRepository.findByAccountIdWithoutAnonymized(existAccountId);
    Optional<User> notExistResult = userRepository.findByAccountIdWithoutAnonymized(notExistAccountId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush()),
        //성공 케이스 - 2 - 존재하는 계정 ID로 찾는 경우
        () -> assertTrue(existResult.isPresent()),
        //실패 케이스 - 1 - 없는 계정 ID로 찾는 경우
        () -> assertTrue(notExistResult.isEmpty())
    );
  }
}