package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;
import com.heylocal.traveler.repository.TravelerProfileRepository;
import com.heylocal.traveler.repository.TravelerRepository;
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
  private static final int signupPointInit = 100000; //회원가입 시, 지급하는 초기 포인트
  private final UserRepository userRepository;
  private final TravelerRepository travelerRepository;
  private final TravelerProfileRepository travelerProfileRepository;
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
   * 휴대폰 번호 중복 확인 메서드
   * 매니저 계정까지 검사
   * </pre>
   * @param phoneNumber 확인할 전화번호
   * @return IdCheckResponse 의 속성 isAlreadyExist 이 true 면 중복
   */
  @Transactional
  public UserInfoCheckResponse checkPhoneNumberExist(String phoneNumber) {
    boolean isExist;
    Optional<User> result;

    result = userRepository.findByPhoneNumber(phoneNumber);
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
  public void signupTraveler(SignupRequest request) throws IllegalArgumentException {
    String accountId = request.getAccountId();
    String nickname = request.getNickname();
    String phoneNumber = request.getPhoneNumber();
    String encodedPassword;

    //중복 확인
    UserInfoCheckResponse accountIdCheckRes = checkAccountIdExist(accountId);
    UserInfoCheckResponse phoneNumCheckRes = checkPhoneNumberExist(phoneNumber);
    if (accountIdCheckRes.isAlreadyExist() || phoneNumCheckRes.isAlreadyExist()) {
      throw new IllegalArgumentException("계정 ID 나, 휴대폰 번호가 이미 존재합니다.");
    }

    //사용자 저장
    encodedPassword = encodePassword(request.getPassword());
    request.setPassword(encodedPassword);
    Traveler traveler = travelerRepository.saveTraveler(accountId, encodedPassword, nickname, phoneNumber);
    travelerProfileRepository.saveTravelerProfile(traveler, signupPointInit, null);
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
