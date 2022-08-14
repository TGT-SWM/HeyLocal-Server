package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.SignupDto;
import com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.heylocal.traveler.dto.SignupDto.*;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  /**
   * <pre>
   * 아이디 중복 확인 메서드
   * </pre>
   * @param accountId 확인할 아이디
   * @return IdCheckResponse 의 속성 isAlreadyExist 이 true 면 중복
   */
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
  public void signupTraveler(SignupRequest request) {

  }
}
