package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.TravelOnDto;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.TravelOnDto.*;

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
    region = regionRepository.findById(request.getRegionId()).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 Region ID 입니다.")
    );
    travelOn = request.toEntity(author, region);
    travelOnRepository.saveTravelOn(travelOn);
  }

  /**
   * 여행On 목록을 여러 조건으로 조회
   * @param request 조회 조건
   * @return
   * @throws BadArgumentException
   */
  @Transactional
  public List<TravelOnSimpleResponse> inquirySimpleTravelOns(AllTravelOnGetRequest request) throws BadArgumentException {
    List<TravelOn> travelOnList;
    List<TravelOnSimpleResponse> response;
    Long regionId = request.getRegionId();

    if (Objects.isNull(regionId)) { //지역 관계없이 조회하는 경우
      travelOnList = findWithoutRegion(request);

    } else { //Region을 기준으로 조회하는 경우
      travelOnList = findByRegion(request);
    }

    //List<TravelOn> -> List<TravelOnSimpleResponse>
    response = travelOnList.stream()
        .map(TravelOnSimpleResponse::new)
        .collect(Collectors.toList());

    return response;
  }

  /**
   * 여행 On 상세 조회
   * @param travelOnId 조회할 여행 On 의 ID
   * @return
   */
  @Transactional
  public TravelOnResponse inquiryTravelOn(long travelOnId) throws BadArgumentException {
    TravelOnResponse response;
    TravelOn travelOn;

    travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );
    response = new TravelOnResponse(travelOn);

    return response;
  }

  private List<TravelOn> findByRegion(AllTravelOnGetRequest request) throws BadArgumentException {
    List<TravelOn> result;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    long regionId;
    Long lastItemId;
    int size;

    //초기화
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    regionId = request.getRegionId();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();

    Region region = regionRepository.findById(regionId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 Region ID 입니다.")
    );

    if (Objects.isNull(withOpinions)) {
      result = travelOnRepository.findAllByRegion(region, lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinionByRegion(region, lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinionByRegion(region, lastItemId, size, sortBy);
    }

    return result;
  }

  private List<TravelOn> findWithoutRegion(AllTravelOnGetRequest request) {
    List<TravelOn> result;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    Long lastItemId;
    int size;

    //초기화
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();

    if (Objects.isNull(withOpinions)) {
      result = travelOnRepository.findAll(lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinion(lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinion(lastItemId, size, sortBy);
    }

    return result;
  }

}
