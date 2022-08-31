package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.TravelOnDto.TravelOnRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelOnService {

  private final TravelOnRepository travelOnRepository;
  private final RegionRepository regionRepository;
  private final UserRepository userRepository;

  /**
   * 새로운 여행On을 등록하는 메서드
   * @param request 등록할 여행On 내용
   * @param loginUser 작성자(로그인된 사용자)
   */
  @Transactional
  public void addNewTravelOn(TravelOnRequest request, LoginUser loginUser) throws BadArgumentException {
    TravelOn travelOn;
    Region region;
    User author;

    author = userRepository.findById(loginUser.getId()).get();
    region = regionRepository.findByStateAndCity(request.getRegion().getState(), request.getRegion().getCity()).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 Region 입니다.")
    );
    travelOn = request.toEntity(author, region);
    travelOnRepository.saveTravelOn(travelOn);
  }

}
