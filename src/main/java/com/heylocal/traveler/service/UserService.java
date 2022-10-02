package com.heylocal.traveler.service;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.UserDto;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.UserMapper;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.dto.UserDto.*;
import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final RegionRepository regionRepository;
  private final S3UrlUserContext s3UserUrlContext;
  private final S3PresignUrlProvider s3PresignUrlProvider;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;

  /**
   * 사용자 프로필을 조회하는 메서드
   * @param userId 조회할 사용자의 ID
   * @return
   */
  @Transactional(readOnly = true)
  public UserProfileResponse inquiryUserProfile(long userId) throws NotFoundException {
    User targetUser;
    UserProfile targetProfile;
    UserProfileResponse responseDto;

    //사용자, 프로필 조회
    targetUser = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID입니다.")
    );
    targetProfile = targetUser.getUserProfile();

    //프로필 응답 DTO 생성
    responseDto = UserMapper.INSTANCE.toUserProfileResponseDto(targetProfile, s3UserUrlContext);

    return responseDto;
  }

  /**
   * 사용자 프로필을 수정하는 메서드
   * @param targetUserId 수정할 프로필을 갖는 사용자 Id
   * @param request 수정 내용
   */
  @Transactional
  public void updateProfile(long targetUserId, UserProfileRequest request) throws NotFoundException {
    int activityRegionId = request.getActivityRegionId();
    Region activityRegion = regionRepository.findById(activityRegionId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 지역 ID 입니다")
    );
    UserProfile userProfile;

    //비즈니스 요구에 맞는 Region 인지 검증
    if (!isValidRegion(activityRegion)) {
      throw new NotFoundException(NotFoundCode.NO_INFO, "잘못된 지역 ID 입니다");
    }

    //수정할 프로필 조회
    userProfile = userRepository.findById(targetUserId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID 입니다.")
    ).getUserProfile();

    //프로필 수정
    UserMapper.INSTANCE.updateUserProfile(request, activityRegion, userProfile);
  }

  /**
   * 프로필을 수정할 수 있는 권한이 있는지 확인하는 메서드
   * @param targetUserId 수정할 프로필을 갖는 사용자 Id
   * @param loginUser 로그인한 사용자
   * @return true:수정가능, false:수정불가
   */
  public boolean canUpdateProfile(long targetUserId, LoginUser loginUser) {
    if (targetUserId != loginUser.getId()) return false;
    return true;
  }

  /**
   * 프로필 이미지를 Put 하는 Presigned URL 을 반환하는 메서드
   * @param userId 사용자 Id
   * @return
   */
  public Map<String, String> getImgPutPresignedUrl(long userId) {
    String objectName = s3ObjectNameFormatter.getObjectNameOfProfileImg(userId);
    String presignedUrl = s3PresignUrlProvider.getPresignedUrl(objectName, HttpMethod.PUT);
    Map<String, String> result = new ConcurrentHashMap<>();

    result.put("imgUploadUrl", presignedUrl);

    return result;
  }

  /**
   * 비즈니스 요구에 맞는 Region 인지 검증하는 메서드
   * @param region 검증할 Region 엔티티
   * @return
   */
  private boolean isValidRegion(Region region) {
    String state = region.getState();
    String city = region.getCity();

    if (state.equals("서울특별시") && city != null) return false;
    if (state.equals("부산광역시") && city != null) return false;
    if (state.equals("대구광역시") && city != null) return false;
    if (state.equals("인천광역시") && city != null) return false;
    if (state.equals("광주광역시") && city != null) return false;
    if (state.equals("대전광역시") && city != null) return false;
    if (state.equals("울산광역시") && city != null) return false;
    if (state.equals("세종특별자치시") && city != null) return false;
    if (state.equals("제주특별자치도") && city != null) return false;
    if (state.endsWith("도") && city.endsWith("구")) return false;

    return true;
  }
}
