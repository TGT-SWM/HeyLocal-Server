package com.heylocal.traveler.service;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.UserDto;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private RegionRepository regionRepository;
  @Mock
  private S3UrlUserContext s3UserUrlContext;
  @Mock
  private S3PresignUrlProvider s3PresignUrlProvider;
  @Mock
  private S3ObjectNameFormatter s3ObjectNameFormatter;
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, regionRepository, s3UserUrlContext, s3PresignUrlProvider, s3ObjectNameFormatter);
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

  @Test
  @DisplayName("사용자 프로필 수정 - 성공 케이스")
  void updateProfileSucceedTest() throws NotFoundException {
    //GIVEN
    long targetUserId = 1L;
    int activityRegionId = 2;
    String newNickname = "myNickname";
    String newIntroduce = "my introduce";
    UserDto.UserProfileRequest request = UserDto.UserProfileRequest.builder()
        .nickname(newNickname)
        .introduce(newIntroduce)
        .activityRegionId(activityRegionId)
        .build();

    //Mock 행동 정의 - regionRepository
    Region region = Region.builder()
        .id(Long.parseLong(Integer.toString(activityRegionId)))
        .state("경기도")
        .city("성남시")
        .build();
    willReturn(Optional.of(region)).given(regionRepository).findById(eq(Long.parseLong(Integer.toString(activityRegionId))));

    //Mock 행동 정의 - userRepository
    UserProfile userProfile = UserProfile.builder().build();
    User user = User.builder().id(targetUserId).build();
    userProfile.setUser(user);
    willReturn(Optional.of(user)).given(userRepository).findById(targetUserId);

    //WHEN
    userService.updateProfile(targetUserId, request);

    //THEN
    assertAll(
        () -> assertEquals(activityRegionId, userProfile.getActivityRegion().getId()),
        () -> assertEquals(newNickname, userProfile.getUser().getNickname()),
        () -> assertEquals(newIntroduce, userProfile.getIntroduce())
    );
  }

  @Test
  @DisplayName("사용자 프로필 수정 - Activity Region Id 가 존재하지 않는 경우")
  void updateProfileNotExistRegionTest() throws NotFoundException {
    //GIVEN
    long targetUserId = 1L;
    int activityRegionId = 2;
    String newNickname = "myNickname";
    String newIntroduce = "my introduce";
    UserDto.UserProfileRequest request = UserDto.UserProfileRequest.builder()
        .nickname(newNickname)
        .introduce(newIntroduce)
        .activityRegionId(activityRegionId)
        .build();

    //Mock 행동 정의 - regionRepository
    willReturn(Optional.empty()).given(regionRepository).findById(eq(Long.parseLong(Integer.toString(activityRegionId))));

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.updateProfile(targetUserId, request));
  }

  @Test
  @DisplayName("사용자 프로필 수정 - 잘못된 Region 인 경우")
  void updateProfileWrongRegionTest() {
    //GIVEN
    long targetUserId = 1L;
    int activityRegionId = 2;
    String newNickname = "myNickname";
    String newIntroduce = "my introduce";
    UserDto.UserProfileRequest request = UserDto.UserProfileRequest.builder()
        .nickname(newNickname)
        .introduce(newIntroduce)
        .activityRegionId(activityRegionId)
        .build();

    //Mock 행동 정의 - regionRepository
    Region region1 = Region.builder()
        .id(Long.parseLong(Integer.toString(activityRegionId)))
        .state("서울특별시")
        .city("강남구")
        .build();
    Region region2 = Region.builder()
        .id(Long.parseLong(Integer.toString(activityRegionId)))
        .state("충청북도")
        .city("상당구")
        .build();
    willReturn(Optional.of(region1)).willReturn(Optional.of(region2))
        .given(regionRepository).findById(eq(Long.parseLong(Integer.toString(activityRegionId))));

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.updateProfile(targetUserId, request));
    assertThrows(NotFoundException.class, () -> userService.updateProfile(targetUserId, request));
  }

  @Test
  @DisplayName("사용자 프로필 수정 - User Id 가 존재하지 않는 경우")
  void updateProfileNotExistUserTest() {
    //GIVEN
    long targetUserId = 1L;
    int activityRegionId = 2;
    String newNickname = "myNickname";
    String newIntroduce = "my introduce";
    UserDto.UserProfileRequest request = UserDto.UserProfileRequest.builder()
        .nickname(newNickname)
        .introduce(newIntroduce)
        .activityRegionId(activityRegionId)
        .build();

    //Mock 행동 정의 - regionRepository
    Region region = Region.builder()
        .id(Long.parseLong(Integer.toString(activityRegionId)))
        .state("경기도")
        .city("성남시")
        .build();
    willReturn(Optional.of(region)).given(regionRepository).findById(eq(Long.parseLong(Integer.toString(activityRegionId))));

    //Mock 행동 정의 - userRepository
    willReturn(Optional.empty()).given(userRepository).findById(targetUserId);

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.updateProfile(targetUserId, request));
  }

  @Test
  @DisplayName("프로필 수정 권한 확인")
  void canUpdateProfileTest() {
    //GIVEN
    long targetUserId = 1L;
    LoginUser validUser = LoginUser.builder().id(targetUserId).build();
    LoginUser invalidUser = LoginUser.builder().id(targetUserId + 1).build();

    //WHEN
    boolean succeedResult = userService.canUpdateProfile(targetUserId, validUser);
    boolean failResult = userService.canUpdateProfile(targetUserId, invalidUser);

    //THEN
    assertAll(
        () -> assertTrue(succeedResult),
        () -> assertFalse(failResult)
    );
  }

  @Test
  @DisplayName("프로필 이미지 업데이트 Presigned URL 생성 - 성공 케이스")
  void getImgUpdatePresignedUrlSucceedTest() throws NotFoundException {
    //GIVEN
    long hasObjectNameUserId = 1L;
    long noObjectNameUserId = 2L;

    //Mock 행동 정의 - userRepository
    String savedObjectName = "objectName";
    UserProfile hasObjectNameUserProfile = UserProfile.builder()
        .imageObjectKeyName(savedObjectName)
        .build();
    UserProfile noObjectNameUserProfile = UserProfile.builder()
        .imageObjectKeyName(null)
        .build();
    User hasObjectNameUser = User.builder().id(hasObjectNameUserId).build();
    User noObjectNameUser = User.builder().id(hasObjectNameUserId).build();
    hasObjectNameUserProfile.setUser(hasObjectNameUser);
    noObjectNameUserProfile.setUser(noObjectNameUser);

    willReturn(Optional.of(hasObjectNameUser)).given(userRepository).findById(hasObjectNameUserId);
    willReturn(Optional.of(noObjectNameUser)).given(userRepository).findById(noObjectNameUserId);

    //Mock 행동 정의 - s3ObjectNameFormatter
    willReturn(savedObjectName).given(s3ObjectNameFormatter).getObjectNameOfProfileImg(anyLong());

    //Mock 행동 정의 - s3PresignUrlProvider
    String putUrl = "putUrl";
    willReturn(putUrl).given(s3PresignUrlProvider).getPresignedUrl(eq(savedObjectName), eq(HttpMethod.PUT));
    String deleteUrl = "deleteUrl";
    willReturn(deleteUrl).given(s3PresignUrlProvider).getPresignedUrl(eq(savedObjectName), eq(HttpMethod.DELETE));

    //WHEN
    Map<String, String> hasObjectNameResult = userService.getImgUpdatePresignedUrl(hasObjectNameUserId);
    Map<String, String> noObjectNameResult = userService.getImgUpdatePresignedUrl(noObjectNameUserId);

    //THEN
    assertAll(
        //기존에 저장된 Object Name 이 있는 경우
        () -> assertTrue(hasObjectNameResult.get("newPutUrl").isEmpty()),
        () -> assertEquals(putUrl, hasObjectNameResult.get("updatePutUrl")),
        () -> assertEquals(deleteUrl, hasObjectNameResult.get("deleteUrl")),
        //기존에 저장된 Object Name 이 없는 경우
        () -> assertTrue(noObjectNameResult.get("updatePutUrl").isEmpty()),
        () -> assertTrue(noObjectNameResult.get("deleteUrl").isEmpty()),
        () -> assertEquals(putUrl, noObjectNameResult.get("newPutUrl"))
    );
  }

  @Test
  @DisplayName("프로필 이미지 업데이트 Presigned URL 생성 - 존재하지 않는 사용자 ID 인 경우")
  void getImgUpdatePresignedUrlNotExistUserTest() {
    //GIVEN
    long notExistUserId = 1L;

    //Mock 행동 정의 - userRepository
    willReturn(Optional.empty()).given(userRepository).findById(notExistUserId);

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.getImgUpdatePresignedUrl(notExistUserId));
  }

  @Test
  @DisplayName("Object Key를 UserProfile 엔티티에 저장 - 성공 케이스")
  void saveProfileObjectKeySucceedTest() throws NotFoundException {
    //GIVEN
    String key = "myKey";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(key).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    String stringUserId = "1";
    Map<S3ObjectNameFormatter.ObjectNameProperty, String> nameFormatResult = new ConcurrentHashMap<>();
    nameFormatResult.put(S3ObjectNameFormatter.ObjectNameProperty.USER_ID, stringUserId);
    willReturn(nameFormatResult).given(s3ObjectNameFormatter).parseObjectNameOfProfileImg(key);

    //Mock 행동 정의 - userRepository
    long userId = Long.parseLong(stringUserId);
    UserProfile userProfile = UserProfile.builder().imageObjectKeyName(null).build();
    User user = User.builder().id(userId).build();
    userProfile.setUser(user);

    willReturn(Optional.of(user)).given(userRepository).findById(eq(userId));

    //WHEN
    userService.saveProfileObjectKey(s3ObjectDto);

    //THEN
    assertEquals(key, userProfile.getImageObjectKeyName());
  }

  @Test
  @DisplayName("Object Key를 UserProfile 엔티티에 저장 - 존재하지 않는 사용자 ID 인 경우")
  void saveProfileObjectKeyNotExistUserTest() throws NotFoundException {
    //GIVEN
    String key = "myKey";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(key).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    String stringUserId = "1";
    Map<S3ObjectNameFormatter.ObjectNameProperty, String> nameFormatResult = new ConcurrentHashMap<>();
    nameFormatResult.put(S3ObjectNameFormatter.ObjectNameProperty.USER_ID, stringUserId);
    willReturn(nameFormatResult).given(s3ObjectNameFormatter).parseObjectNameOfProfileImg(key);

    //Mock 행동 정의 - userRepository
    willReturn(Optional.empty()).given(userRepository).findById(anyLong());

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.saveProfileObjectKey(s3ObjectDto));
  }

  @Test
  @DisplayName("Object Key를 UserProfile 엔티티에서 제거 - 성공 케이스")
  void removeProfileObjectKeySucceedTest() throws NotFoundException {
    //GIVEN
    String key = "myKey";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(key).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    String stringUserId = "1";
    Map<S3ObjectNameFormatter.ObjectNameProperty, String> nameFormatResult = new ConcurrentHashMap<>();
    nameFormatResult.put(S3ObjectNameFormatter.ObjectNameProperty.USER_ID, stringUserId);
    willReturn(nameFormatResult).given(s3ObjectNameFormatter).parseObjectNameOfProfileImg(key);

    //Mock 행동 정의 - userRepository
    long userId = Long.parseLong(stringUserId);
    UserProfile userProfile = UserProfile.builder().imageObjectKeyName(null).build();
    User user = User.builder().id(userId).build();
    userProfile.setUser(user);

    willReturn(Optional.of(user)).given(userRepository).findById(eq(userId));

    //WHEN
    userService.removeProfileObjectKey(s3ObjectDto);

    //THEN
    assertNull(userProfile.getImageObjectKeyName());
  }

  @Test
  @DisplayName("Object Key를 UserProfile 엔티티에서 제거 - 존재하지 않는 사용자 Id 인 경우")
  void removeProfileObjectKeyNotExistUserTest() {
    //GIVEN
    String key = "myKey";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(key).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    String stringUserId = "1";
    Map<S3ObjectNameFormatter.ObjectNameProperty, String> nameFormatResult = new ConcurrentHashMap<>();
    nameFormatResult.put(S3ObjectNameFormatter.ObjectNameProperty.USER_ID, stringUserId);
    willReturn(nameFormatResult).given(s3ObjectNameFormatter).parseObjectNameOfProfileImg(key);

    //Mock 행동 정의 - userRepository
    willReturn(Optional.empty()).given(userRepository).findById(anyLong());

    //WHEN

    //THEN
    assertThrows(NotFoundException.class, () -> userService.removeProfileObjectKey(s3ObjectDto));
  }
}