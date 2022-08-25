package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;
import com.heylocal.traveler.exception.code.SignupCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.UserProfileRepository;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.heylocal.traveler.dto.SignupDto.SignupRequest;

/**
 * 회원가입 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class SignupService {
  private final UserRepository userRepository;
  private final UserProfileRepository userProfileRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * <pre>
   * 아이디 중복 확인 메서드
   * </pre>
   * @param accountId 확인할 아이디
   * @return IdCheckResponse 의 속성 isAlreadyExist 이 true 면 중복
   */
  @Transactional
  public UserInfoCheckResponse checkAccountIdExist(String accountId) {
    boolean isExist;
    Optional<User> result;

    result = userRepository.findByAccountId(accountId);
    isExist = result.isPresent();


    return new UserInfoCheckResponse(isExist);
  }

  /**
   * <pre>
   * 사용자(여행자)를 회원가입 시키는 메서드
   * </pre>
   * @param request
   */
  @Transactional
  public void signupUser(SignupRequest request) throws BadArgumentException {
    String accountId = request.getAccountId();
    String nickname = request.getNickname();
    String encodedPassword;

    //중복 확인
    UserInfoCheckResponse accountIdCheckRes = checkAccountIdExist(accountId);
    if (accountIdCheckRes.isAlreadyExist()) {
      throw new BadArgumentException(SignupCode.ALREADY_EXIST_USER_INFO);
    }

    //사용자 저장
    encodedPassword = encodePassword(request.getPassword());
    request.setPassword(encodedPassword);
    User user = userRepository.saveUser(accountId, encodedPassword, nickname, UserRole.TRAVELER);
    userProfileRepository.saveUserProfile(user.getId(), 0, null);
  }

  /**
   * 비밀번호 암호화 메서드
   * @param rawPassword 암호화시킬 원본 비밀번호
   * @return 암호화된 비밀번호
   */
  private String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}
