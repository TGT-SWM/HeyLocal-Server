package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private S3UrlUserContext s3UserUrlContext;
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, s3UserUrlContext);
  }

  @Test
  @DisplayName("사용자 프로필 조회")
  void inquiryUserProfileTest() throws NotFoundException {
    //GIVEN
    long existUserId = 1L;
    long notExistUserId = 2L;
    User user = User.builder()
        .id(existUserId)
        .accountId("myAccountId")
        .password("myPassword")
        .nickname("myNickname")
        .userRole(UserRole.TRAVELER)
        .build();
    String profileIntroduce = "my introduce";
    UserProfile userProfile = UserProfile.builder()
        .id(3L)
        .introduce(profileIntroduce)
        .build();
    userProfile.setUser(user);

    //Mock 행동 정의 - userRepository
    willReturn(Optional.of(user)).given(userRepository).findById(existUserId);
    willReturn(Optional.empty()).given(userRepository).findById(notExistUserId);

    //WHEN
    UserProfileResponse result = userService.inquiryUserProfile(existUserId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 사용자 Id 로 조회 시
        () -> assertSame(profileIntroduce, result.getIntroduce()),
        //실패 케이스 - 1 - 존재하지 않는 사용자 Id 로 조회 시
        () -> assertThrows(NotFoundException.class, () -> userService.inquiryUserProfile(notExistUserId))
    );
  }
}