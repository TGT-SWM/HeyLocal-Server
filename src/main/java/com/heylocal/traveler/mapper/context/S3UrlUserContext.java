/**
 * packageName    : com.heylocal.traveler.mapper.context
 * fileName       : S3UrlUserContext
 * author         : 우태균
 * date           : 2022/10/02
 * description    : 사용자, 프로필에 관련 이미지의 S3 접근 URL을 바인딩하는 Mapping Context
 */

package com.heylocal.traveler.mapper.context;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.UserDto;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3UrlUserContext {
  private final S3PresignUrlProvider s3PresignUrlProvider;

  @AfterMapping
  public void bindS3DownloadUrl(@MappingTarget UserDto.UserProfileResponse userProfileResponse, User user) {
    String imgKey = user.getUserProfile().getImageObjectKeyName();
    if (imgKey != null) {
      String downloadUrl = s3PresignUrlProvider.getPresignedUrl(imgKey, HttpMethod.GET);
      userProfileResponse.setProfileImgDownloadUrl(downloadUrl);
    }
  }

  @AfterMapping
  public void bindS3DownloadUrl(@MappingTarget UserDto.UserProfileResponse userProfileResponse, UserProfile userProfile) {
    String imgKey = userProfile.getImageObjectKeyName();
    if (imgKey != null) {
      String downloadUrl = s3PresignUrlProvider.getPresignedUrl(imgKey, HttpMethod.GET);
      userProfileResponse.setProfileImgDownloadUrl(downloadUrl);
    }
  }
}