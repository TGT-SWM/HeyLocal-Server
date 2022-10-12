package com.heylocal.traveler.mapper.context;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

class S3UrlUserContextTest {
  @Mock
  private S3PresignUrlProvider s3PresignUrlProvider;
  private S3UrlUserContext s3UrlUserContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    s3UrlUserContext = new S3UrlUserContext(s3PresignUrlProvider);
  }

  @Test
  @DisplayName("유저 프로필 DTO에 유저 엔티티로 이미지 다운로드 URL 바인딩")
  void bindS3DownloadUrlByUserTest() {
    //GIVEN
    String objectKey = "my object key";
    UserProfileResponse dto = new UserProfileResponse();
    UserProfile userProfileNotNullKey = UserProfile.builder()
        .imageObjectKeyName(objectKey)
        .build();
    User userNotNullKey = User.builder()
        .userProfile(userProfileNotNullKey)
        .build();
    UserProfile userProfileNullKey = UserProfile.builder()
        .imageObjectKeyName(null)
        .build();
    User userNullKey = User.builder()
        .userProfile(userProfileNullKey)
        .build();
    UserProfileResponse resultNotNullKey = new UserProfileResponse();
    UserProfileResponse resultNullKey = new UserProfileResponse();

    //Mock 행동 정의
    String downloadUrl = "my download url";
    willReturn(downloadUrl).given(s3PresignUrlProvider).getPresignedUrl(eq(objectKey), eq(HttpMethod.GET));

    //WHEN
    s3UrlUserContext.bindS3DownloadUrl(resultNotNullKey, userNotNullKey);
    s3UrlUserContext.bindS3DownloadUrl(resultNullKey, userNullKey);

    //THEN
    assertAll(
        //성공 케이스 - 1 - Object Key 가 존재하는 경우
        () -> assertEquals(downloadUrl, resultNotNullKey.getProfileImgDownloadUrl()),
        //성공 케이스 - 2 - Object Key 가 존재하지 않는 경우
        () -> assertNull(resultNullKey.getProfileImgDownloadUrl())
    );
  }

  @Test
  @DisplayName("유저 프로필 DTO에 유저 프로필 엔티티로 이미지 다운로드 URL 바인딩")
  void bindS3DownloadUrlByUserProfileTest() {
    //GIVEN
    String objectKey = "my object key";
    UserProfileResponse dto = new UserProfileResponse();
    UserProfile userProfileNotNullKey = UserProfile.builder()
        .imageObjectKeyName(objectKey)
        .build();
    UserProfile userProfileNullKey = UserProfile.builder()
        .imageObjectKeyName(null)
        .build();
    UserProfileResponse resultNotNullKey = new UserProfileResponse();
    UserProfileResponse resultNullKey = new UserProfileResponse();

    //Mock 행동 정의
    String downloadUrl = "my download url";
    willReturn(downloadUrl).given(s3PresignUrlProvider).getPresignedUrl(eq(objectKey), eq(HttpMethod.GET));

    //WHEN
    s3UrlUserContext.bindS3DownloadUrl(resultNotNullKey, userProfileNotNullKey);
    s3UrlUserContext.bindS3DownloadUrl(resultNullKey, userProfileNullKey);

    //THEN
    assertAll(
        //성공 케이스 - 1 - Object Key 가 존재하는 경우
        () -> assertEquals(downloadUrl, resultNotNullKey.getProfileImgDownloadUrl()),
        //성공 케이스 - 2 - Object Key 가 존재하지 않는 경우
        () -> assertNull(resultNullKey.getProfileImgDownloadUrl())
    );
  }

}