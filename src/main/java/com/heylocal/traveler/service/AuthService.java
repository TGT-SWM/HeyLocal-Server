package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.UserType;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.code.TokenCode;
import com.heylocal.traveler.exception.service.TokenException;
import com.heylocal.traveler.repository.TravelerRepository;
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
  public LoginUser findLoginTraveler(long travelerId) throws TokenException {
    Traveler traveler = travelerRepository.findById(travelerId).orElseThrow(
        () -> new TokenException(TokenCode.NOT_EXIST_TOKEN_USER_ID)
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
