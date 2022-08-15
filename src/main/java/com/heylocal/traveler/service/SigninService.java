package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.service.exception.BadArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigninService {

  private final TravelerRepository travelerRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 로그인 메서드
   * @param request 요청 DTO
   * @return 로그인 성공 시, 응답 DTO
   * @throws IllegalArgumentException 로그인 실패 시
   */
  @Transactional
  public SigninResponse signin(SigninRequest request) throws BadArgumentException {
    String accountId = request.getAccountId();
    String rawPassword = request.getPassword();
    Traveler traveler;
    SigninResponse response;

    //계정 id 확인
    traveler = checkAccountId(accountId);

    //비밀번호 확인
    traveler = checkPassword(rawPassword, traveler);

    response = SigninResponse.builder()
        .id(traveler.getId())
        .accountId(traveler.getAccountId())
        .nickname(traveler.getNickname())
        .phoneNumber(traveler.getPhoneNumber())
        .userType(UserType.TRAVELER)
        .build();
    return response;
  }

  /**
   * 계정 ID 확인 메서드
   * @param accountId (client가 요청한) 확인할 계정 ID
   * @return 해당 계정 ID를 가지고 있는 Traveler 엔티티
   * @throws IllegalArgumentException 존재하지 않는 계정 ID인 경우
   */
  private Traveler checkAccountId(String accountId) throws BadArgumentException {
    Optional<Traveler> travelerByAccountId;

    travelerByAccountId = travelerRepository.findByAccountId(accountId);
    if (travelerByAccountId.isEmpty()) {
      throw new BadArgumentException("존재하지 않는 계정 정보입니다.");
    }

    return travelerByAccountId.get();
  }

  /**
   * 비밀번호 확인 메서드
   * @param rawPassword (client가 요청한) 확인할 비밀번호
   * @param travelerByAccountId 요청받은 계정 ID로 찾은 Traveler 엔티티, 비밀번호를 대조할 엔티티
   * @return 비밀번호가 일치하는 경우 해당 Traveler 엔티티 반환
   * @exception IllegalArgumentException 비밀번호가 일치하지 않는 경우
   */
  private Traveler checkPassword(String rawPassword, Traveler travelerByAccountId) throws BadArgumentException {
    String encodedPasswordOfFoundTraveler;
    boolean isMatchedPassword;

    encodedPasswordOfFoundTraveler = travelerByAccountId.getPassword();
    isMatchedPassword = checkPasswordMatch(rawPassword, encodedPasswordOfFoundTraveler);
    if (!isMatchedPassword) {
      throw new BadArgumentException("존재하지 않는 계정 정보입니다.");
    }

    return travelerByAccountId;
  }

  /**
   * '일반 비밀번호'와 '인코딩된 비밀번호'가 서로 일치하는지 비교하는 메서드
   * @param rawPassword 일반 비밀번호
   * @param encodedPassword 인코딩된 비밀번호
   * @return 결과, true:일치, false:불일치
   */
  private boolean checkPasswordMatch(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
