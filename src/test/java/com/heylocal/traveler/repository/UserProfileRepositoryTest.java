package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import({UserProfileRepository.class})
@DataJpaTest
class UserProfileRepositoryTest {
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("사용자 프로필 저장")
  void saveUserProfileTest() {
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
    int knowHow = 1000;
    UserProfile userProfile = UserProfile.builder()
            .user(user)
            .knowHow(knowHow)
            .build();

    em.persist(user);

    //WHEN - THEN
    userProfileRepository.saveUserProfile(userProfile);

    assertAll(
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush()),
        //성공 케이스 - 2 - 프로필에 사용자가 연관되었는지 확인
        () -> assertSame(user, userProfile.getUser()),
        //성공 케이스 - 3 - 프로필의 노하우 필드가 제대로 설정되었는지 확인
        () -> assertEquals(knowHow, userProfile.getKnowHow())
    );
  }

  @Test
  @DisplayName("노하우 순으로 조회")
  void findSortedByKnowHowDescTest() {
    //GIVEN
    User user1 = User.builder()
        .accountId("accountId1")
        .password("encodedPw1")
        .nickname("nickname1")
        .userRole(UserRole.TRAVELER)
        .build();
    User user2 = User.builder()
        .accountId("accountId2")
        .password("encodedPw2")
        .nickname("nickname2")
        .userRole(UserRole.TRAVELER)
        .build();
    em.persist(user1);
    em.persist(user2);
    int knowHow1 = 1000;
    int knowHow2 = 2000;
    UserProfile userProfile1 = UserProfile.builder()
        .user(user1)
        .knowHow(knowHow1)
        .build();
    UserProfile userProfile2 = UserProfile.builder()
        .user(user2)
        .knowHow(knowHow2)
        .build();

    em.persist(userProfile1);
    em.persist(userProfile2);

    //WHEN
    List<UserProfile> result = userProfileRepository.findSortedByKnowHowDesc(2);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertSame(2, result.size()),
        () -> assertEquals(knowHow2, result.get(0).getKnowHow()),
        () -> assertEquals(knowHow1, result.get(1).getKnowHow())
    );
  }
}