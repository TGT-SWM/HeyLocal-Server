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
    region = regionRepository.findByStateAndCity(request.getRegion().getState(), request.getRegion().getCity()).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 Region 입니다.")
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
    String state = request.getState();
    String city = request.getCity();

    if (Objects.isNull(state)) { //지역 관계없이 조회하는 경우
      travelOnList = findWithoutRegion(request);

    } else if (Objects.isNull(city)) { //state만을 기준으로 조회하는 경우
      travelOnList = findWithOnlyState(request);

    } else { //state와 city를 기준으로 조회하는 경우
      travelOnList = findWithStateAndCity(request);
    }

    //List<TravelOn> -> List<TravelOnSimpleResponse>
    response = travelOnList.stream()
        .map(TravelOnSimpleResponse::new)
        .collect(Collectors.toList());

    return response;
  }

  private List<TravelOn> findWithStateAndCity(AllTravelOnGetRequest request) throws BadArgumentException {
    List<TravelOn> result;
    String state;
    String city;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    Long lastItemId;
    int size;

    //초기화
    state = request.getState();
    city = request.getCity();
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();

    Region region = regionRepository.findByStateAndCity(state, city).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 Region 입니다.")
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

  private List<TravelOn> findWithOnlyState(AllTravelOnGetRequest request) throws BadArgumentException {
    List<TravelOn> result;
    String state;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    Long lastItemId;
    int size;

    //초기화
    state = request.getState();
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();

    if (regionRepository.findByState(state).size() == 0) {
      throw new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 State 입니다.");
    }

    if (Objects.isNull(withOpinions)) {
      result = travelOnRepository.findAllByState(state, lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinionByState(state, lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinionByState(state, lastItemId, size, sortBy);
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
