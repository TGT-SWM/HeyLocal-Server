package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.UserMapper;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  /**
   * 사용자 프로필을 조회하는 메서드
   * @param userId 조회할 사용자의 ID
   * @return
   */
  @Transactional(readOnly = true)
  public UserProfileResponse inquiryUserProfile(long userId) throws NotFoundException {
    User targetUser;
    UserProfile targetProfile;
    int acceptOpinionCount = 0;

    //사용자, 프로필 조회
    targetUser = userRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 사용자 ID입니다.")
    );
    targetProfile = targetUser.getUserProfile();

    int acceptedOpinionCount = countAcceptedOpinion(targetUser);

    return UserMapper.INSTANCE.toUserProfileResponseDto(targetProfile, acceptedOpinionCount);
  }

  private int countAcceptedOpinion(User user) {
    int count = user.getOpinionList().stream().mapToInt(Opinion::getCountAccept).sum();
    return count;
  }
}
