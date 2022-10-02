package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.UserMapper;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final S3UrlUserContext s3UserUrlContext;

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
}
