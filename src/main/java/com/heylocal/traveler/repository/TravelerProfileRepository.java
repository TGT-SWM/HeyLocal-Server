package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.TravelerProfile;
import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TravelerProfileRepository {
  private final EntityManager em;

  /**
   * 사용자(여행자)프로필 저장 메서드
   * @param traveler 관련 User(Traveler)
   * @param possessionPoint 보유 포인트
   * @param imageUrl 프로필 이미지 S3 주소 (nullable)
   * @return 저장된 TravelerProfile 엔티티
   */
  public TravelerProfile saveTravelerProfile(Traveler traveler, int possessionPoint, @Nullable String imageUrl) {
    TravelerProfile travelerProfile = TravelerProfile.builder()
        .user(traveler)
        .possessionPoint(possessionPoint)
        .imageUrl(imageUrl)
        .build();
    em.persist(travelerProfile);

    return travelerProfile;
  }
}
