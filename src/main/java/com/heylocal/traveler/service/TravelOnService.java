package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.TravelTypeGroup;
import com.heylocal.traveler.domain.travelon.list.HopeAccommodation;
import com.heylocal.traveler.domain.travelon.list.HopeDrink;
import com.heylocal.traveler.domain.travelon.list.HopeFood;
import com.heylocal.traveler.domain.travelon.list.TravelMember;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.exception.service.TaskRejectException;
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
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public TravelOnResponse inquiryTravelOn(long travelOnId) throws BadArgumentException {
    TravelOnResponse response;
    TravelOn travelOn;

    travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );
    response = new TravelOnResponse(travelOn);

    return response;
  }

  /**
   * 여행On 수정
   * @param request 수정 정보
   * @param travelOnId 수정할 여행On ID
   * @throws BadArgumentException
   */
  @Transactional
  public void updateTravelOn(TravelOnRequest request, long travelOnId) throws BadArgumentException {
    TravelOn originTravelOn;

    originTravelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    originTravelOn.updateTitle(request.getTitle());
    originTravelOn.updateDescription(request.getDescription());
    originTravelOn.updateTravelStartDate(request.getTravelStartDate());
    originTravelOn.updateTravelEndDate(request.getTravelEndDate());
    originTravelOn.updateTransportationType(request.getTransportationType());
    originTravelOn.updateAccommodationMaxCost(request.getAccommodationMaxCost());
    originTravelOn.updateFoodMaxCost(request.getFoodMaxCost());
    originTravelOn.updateDrinkMaxCost(request.getDrinkMaxCost());

    updateTravelTypeGroup(request, originTravelOn);
    updateTravelMembers(request, originTravelOn);
    updateHopeAccommodation(request, originTravelOn);
    updateHopeFood(request, originTravelOn);
    updateHopeDrink(request, originTravelOn);
  }

  /**
   * 해당 여행On 의 작성자인지 확인
   * @param userId 확인할 사용자 ID
   * @param travelOnId 확인할 여행On ID
   * @return
   * @throws BadArgumentException 존재하지 않는 여행On ID 라면
   */
  @Transactional(readOnly = true)
  public boolean isAuthor(long userId, long travelOnId) throws BadArgumentException {
    TravelOn travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    if (travelOn.getAuthor().getId() == userId) {
      return true;
    }

    return false;
  }

  /**
   * 여행 On 삭제
   * @param targetId 삭제할 여행 On id(pk)
   */
  @Transactional
  public void removeTravelOn(long targetId) throws BadArgumentException, TaskRejectException {
    TravelOn target;

    target = travelOnRepository.findById(targetId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //의견이 달려있다면 삭제 거절
    if (target.getOpinionList().size() != 0) {
      throw new TaskRejectException(ForbiddenCode.NO_PERMISSION, "답변이 달린 여행On 은 삭제할 수 없습니다.");
    }

    //여행 On 삭제
    travelOnRepository.remove(target);
  }

  private void updateTravelTypeGroup(TravelOnRequest request, TravelOn originTravelOn) {
    TravelTypeGroup travelTypeGroup = request.getTravelTypeGroup().toEntity();
    travelTypeGroup.registerAt(originTravelOn);
    originTravelOn.updateTravelTypeGroup(travelTypeGroup);
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

  private void updateTravelMembers(TravelOnRequest request, TravelOn originTravelOn) {
    originTravelOn.removeAllTravelMember();
    request.getMemberTypeSet().stream().forEach(
        (item) -> {
          TravelMember travelMember = TravelMember.builder().memberType(item).build();
          travelMember.registerAt(originTravelOn);
        }
    );
  }

  private void updateHopeAccommodation(TravelOnRequest request, TravelOn originTravelOn) {
    originTravelOn.removeAllHopeAccommodation();
    request.getAccommodationTypeSet().stream().forEach(
        (item) -> {
          HopeAccommodation hopeAccommodation = HopeAccommodation.builder().type(item).build();
          hopeAccommodation.registerAt(originTravelOn);
        }
    );
  }

  private void updateHopeDrink(TravelOnRequest request, TravelOn originTravelOn) {
    originTravelOn.removeAllHopeDrink();
    request.getDrinkTypeSet().stream().forEach(
        (item) -> {
          HopeDrink hopeDrink = HopeDrink.builder().type(item).build();
          hopeDrink.registerAt(originTravelOn);
        }
    );
  }

  private void updateHopeFood(TravelOnRequest request, TravelOn originTravelOn) {
    originTravelOn.removeAllHopeFood();
    request.getFoodTypeSet().stream().forEach(
        (item) -> {
          HopeFood hopeFood = HopeFood.builder().type(item).build();
          hopeFood.registerAt(originTravelOn);
        }
    );
  }

}
