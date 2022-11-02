/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : UserService
 * author         : 우태균
 * date           : 2022/10/01
 * description    : 사용자 관련 서비스
 */

package com.heylocal.traveler.service;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.UserMapper;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.UserProfileRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.UserDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserProfileRepository userProfileRepository;
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
   * 프로필 이미지를 업데이트하는 Presigned URL 을 반환하는 메서드
   * @param userId 사용자 Id
   * @return
   */
  public Map<String, String> getImgUpdatePresignedUrl(long userId) throws NotFoundException {
    String savedObjectName;
    String objectName;
    String putUrl;
    String deleteUrl;
    Map<String, String> result = new ConcurrentHashMap<>();

    //기존에 저장된 Object Key(Name) 조회
    savedObjectName = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID입니다.")
    ).getUserProfile().getImageObjectKeyName();
    //새 Object Key(Name) 생성
    objectName = s3ObjectNameFormatter.getObjectNameOfProfileImg(userId);
    //Put Presigned URL 생성
    putUrl = s3PresignUrlProvider.getPresignedUrl(objectName, HttpMethod.PUT);
    //Delete Presigned URL 생성
    deleteUrl = s3PresignUrlProvider.getPresignedUrl(objectName, HttpMethod.DELETE);

    if (savedObjectName == null) {       //저장된 Object Key(Name) 이 없다면
      result.put("newPutUrl", putUrl);
      result.put("updatePutUrl", "");
      result.put("deleteUrl", "");
    } else {                             //저장된 Object Key(Name) 이 있다면
      result.put("newPutUrl", "");
      result.put("updatePutUrl", putUrl);
      result.put("deleteUrl", deleteUrl);
    }

    return result;
  }

  /**
   * 해당 Key를 UserProfile 엔티티에 저장하는 메서드
   * @param s3ObjectDto key 정보
   */
  @Transactional
  public void saveProfileObjectKey(S3ObjectDto s3ObjectDto) throws NotFoundException {
    //사용자 프로필 엔티티 조회
    UserProfile targetProfile = inquiryUserProfileByObjectKey(s3ObjectDto);

    //해당 프로필 엔티티에 Object Key(Name) 설정
    targetProfile.setImageObjectKeyName(s3ObjectDto.getKey());
  }

  /**
   * 해당 Key를 UserProfile 엔티티에서 제거하는 메서드
   * @param s3ObjectDto key 정보
   */
  @Transactional
  public void removeProfileObjectKey(S3ObjectDto s3ObjectDto) throws NotFoundException {
    //사용자 프로필 엔티티 조회
    UserProfile targetProfile = inquiryUserProfileByObjectKey(s3ObjectDto);

    //해당 프로필 엔티티에서 Object Key(Name) 제거
    targetProfile.setImageObjectKeyName(null);
  }

  /**
   * 노하우 내림차순으로 UserProfile 을 조회하는 메서드
   * @return
   */
  @Transactional(readOnly = true)
  public List<UserProfileResponse> inquiryUserProfileByKnowHowDesc() {
    int size = 30;
    List<UserProfile> entityResult = userProfileRepository.findSortedByKnowHowDesc(size);
    List<UserProfileResponse> result = entityResult.stream()
        .map((item) -> UserMapper.INSTANCE.toUserProfileResponseDto(item, s3UserUrlContext))
        .collect(Collectors.toList());

    return result;
  }

  /**
   * 사용자 ID(pk)로 조회하는 메서드
   * @param userId 조회할 id
   * @return
   * @throws NotFoundException 존재하지 않는 ID(pk)인 경우
   */
  @Transactional(readOnly = true)
  public UserResponse inquiryUser(long userId) throws NotFoundException {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID입니다.")
    );

    return UserMapper.INSTANCE.toUserResponseDto(user);
  }

  /**
   * 해당 사용자의 정보를 익명화하는 메서드
   * @param userId 익명화할 사용자 ID(pk)
   */
  @Transactional
  public void anonymizeUser(long userId) throws NotFoundException {
    User targetUser = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID입니다.")
    );

    //익명화
    targetUser.setNickname("알 수 없는 사용자");
    targetUser.setUserRole(UserRole.ANONYMIZED);
    targetUser.getUserProfile().setIntroduce(null);
    targetUser.getUserProfile().setKnowHow(0);
    targetUser.getUserProfile().releaseActivityRegion();
  }

  /**
   * Object Key(Name)으로 사용자 프로필 엔티티를 조회하는 메서드
   * @param s3ObjectDto
   * @return
   * @throws NotFoundException
   */
  private UserProfile inquiryUserProfileByObjectKey(S3ObjectDto s3ObjectDto) throws NotFoundException {
    String key;
    String stringUserId;
    long userId;
    User targetUser;
    UserProfile targetProfile;

    //해당 사용자의 Id 조회
    key = s3ObjectDto.getKey();
    stringUserId =
        s3ObjectNameFormatter.parseObjectNameOfProfileImg(key).get(S3ObjectNameFormatter.ObjectNameProperty.USER_ID);
    userId = Long.parseLong(stringUserId);

    //해당 사용자의 프로필 엔티티 조회
    targetUser = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID 입니다.")
    );
    targetProfile = targetUser.getUserProfile();
    return targetProfile;
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
