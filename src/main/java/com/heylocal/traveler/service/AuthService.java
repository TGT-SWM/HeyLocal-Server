package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.repository.TravelerRepository;
import com.heylocal.traveler.service.exception.AuthArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인가 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthService {
  private final TravelerRepository travelerRepository;

  /**
   * id값(pk)으로 Traveler를 조회하는 메서드
   * @param travelerId id값 (pk)
   * @return 로그인된 유저의 정보
   */
  @Transactional
  public LoginUser findLoginUser(long travelerId) throws AuthArgumentException {
    Traveler traveler = travelerRepository.findById(travelerId).orElseThrow(
        () -> new AuthArgumentException("Token 클레임의 사용자 pk값이 올바르지 않습니다.")
    );

    return LoginUser.builder()
        .id(traveler.getId())
        .accountId(traveler.getAccountId())
        .nickname(traveler.getNickname())
        .phoneNumber(traveler.getPhoneNumber())
        .userType(UserType.TRAVELER)
        .build();
  }
}
