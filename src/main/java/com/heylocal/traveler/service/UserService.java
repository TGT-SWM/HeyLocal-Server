package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.SignupDto.IdCheckResponse;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
  public IdCheckResponse checkAccountIdExist(String accountId) {
    boolean isExist;
    Optional<User> result;

    result = userRepository.findByAccountId(accountId);
    isExist = result.isPresent();


    return new IdCheckResponse(isExist);
  }
}
