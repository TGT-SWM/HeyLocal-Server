/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : TravelOnService
 * author         : 우태균
 * date           : 2022/08/30
 * description    : 여행 On 관련 서비스
 */

package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.PlanDto;
import com.heylocal.traveler.dto.PlanDto.PlanResponse;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.PlanMapper;
import com.heylocal.traveler.mapper.TravelOnMapper;
import com.heylocal.traveler.mapper.context.S3UrlUserContext;
import com.heylocal.traveler.repository.RegionRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.TravelOnDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelOnService {

  private final TravelOnRepository travelOnRepository;
  private final RegionRepository regionRepository;
  private final UserRepository userRepository;
  private final S3UrlUserContext s3UserUrlContext;

  /**
   * 새로운 여행On을 등록하는 메서드
   * @param request 등록할 여행On 내용
   * @param loginUser 작성자(로그인된 사용자)
   * @exception NotFoundException
   */
  @Transactional
  public void addNewTravelOn(TravelOnRequest request, LoginUser loginUser) throws NotFoundException {
    TravelOn travelOn;
    Region region;
    User author;

    author = userRepository.findById(loginUser.getId()).get();
    region = regionRepository.findById(request.getRegionId()).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 Region ID 입니다.")
    );
    travelOn = TravelOnMapper.INSTANCE.toEntity(request, author, region);
    travelOnRepository.saveTravelOn(travelOn);
  }

  /**
   * 여행On 목록을 여러 조건으로 조회
   * @param request 조회 조건
   * @return
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public List<TravelOnSimpleResponse> inquirySimpleTravelOns(AllTravelOnGetRequest request) throws NotFoundException {
    List<TravelOn> travelOnList;
    List<TravelOnSimpleResponse> response;
    Long regionId = request.getRegionId();
    String keyword = request.getKeyword();

    if (keyword == null || keyword.isEmpty()) {     //키워드가 없는 경우
      if (regionId == null) {                       //지역 관계없이 조회하는 경우
        travelOnList = findWithoutRegionAndKeyword(request);

      } else {                                      //Region을 기준으로 조회하는 경우
        travelOnList = findByRegionWithoutKeyword(request);
      }

    } else {                                        //키워드가 있는 경우
      if (regionId == null) {                       //지역 관계없이 조회하는 경우
        travelOnList = findByKeywordWithoutRegion(request);

      } else {                                      //Region을 기준으로 조회하는 경우
        travelOnList = findByRegionAndKeyword(request);
      }
    }

    //List<TravelOn> -> List<TravelOnSimpleResponse>
    response = travelOnList.stream()
        .map((travelOn) -> TravelOnMapper.INSTANCE.toTravelOnSimpleResponseDto(travelOn, s3UserUrlContext))
        .collect(Collectors.toList());

    return response;
  }

  /**
   * 여행 On 목록을 사용자 ID로 조회
   * @param userId 사용자 ID
   * @param pageRequest 요청하는 페이지 정보
   * @return 여행 On 목록
   */
  public List<TravelOnSimpleResponse> inquirySimpleTravelOns(long userId, PageRequest pageRequest) {
    // 여행 On 조회
    List<TravelOn> travelOns = travelOnRepository.findAllByUserId(
            userId,
            pageRequest.getLastItemId(),
            pageRequest.getSize(),
            TravelOnSortType.DATE
    );

    // List<TravelOn> -> List<TravelOnSimpleResponse>
    return travelOns.stream()
            .map((travelOn) -> TravelOnMapper.INSTANCE.toTravelOnSimpleResponseDto(travelOn, s3UserUrlContext))
            .collect(Collectors.toList());
  }

  /**
   * 여행 On 상세 조회
   * @param travelOnId 조회할 여행 On 의 ID
   * @return
   * @exception NotFoundException
   */
  @Transactional
  public TravelOnResponse inquiryTravelOn(long travelOnId) throws NotFoundException {
    TravelOnResponse response;
    TravelOn travelOn;

    //여행 On 조회
    travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //조회수 1 증가
    travelOn.incrViewsByOne();

    //DTO로 변환
    response = TravelOnMapper.INSTANCE.toTravelOnResponseDto(travelOn, s3UserUrlContext);

    return response;
  }

  @Transactional
  public PlanResponse inquiryRelatedPlan(long travelOnId, long userId) throws ForbiddenException, NotFoundException {
    // 여행 On 조회
    TravelOn travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
            () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    // 조회 권한 검사
    if (userId != travelOn.getAuthor().getId()) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "작성자만 조회할 수 있습니다.");
    }

    // 플랜 조회
    Plan plan = travelOn.getPlan();

    // DTO 변환 후 반환
    return (plan == null)
            ? null
            : PlanMapper.INSTANCE.toPlanResponseDto(plan);
  }

  /**
   * 여행On 수정
   * @param request 수정 정보
   * @param travelOnId 수정할 여행On ID
   * @throws NotFoundException
   */
  @Transactional
  public void updateTravelOn(TravelOnRequest request, long travelOnId) throws NotFoundException {
    TravelOn originTravelOn;

    originTravelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //여행On 업데이트
    TravelOnMapper.INSTANCE.updateTravelOn(request, originTravelOn);

    //연관 플랜이 있는 경우 업데이트
    Plan plan = originTravelOn.getPlan();
    if (plan != null) {
      plan.updateTravelStartDate(request.getTravelStartDate());
      plan.updateTravelEndDate(request.getTravelEndDate());
      plan.updateTransportationType(request.getTransportationType());
    }
  }

  /**
   * 해당 여행On 의 작성자인지 확인
   * @param userId 확인할 사용자 ID
   * @param travelOnId 확인할 여행On ID
   * @return
   * @throws NotFoundException 존재하지 않는 여행On ID 라면
   */
  @Transactional(readOnly = true)
  public boolean isAuthor(long userId, long travelOnId) throws NotFoundException {
    TravelOn travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    if (travelOn.getAuthor().getId() == userId) {
      return true;
    }

    return false;
  }

  /**
   * 여행 On 삭제
   * @param targetId 삭제할 여행 On id(pk)
   * @throws NotFoundException
   * @throws ForbiddenException
   */
  @Transactional
  public void removeTravelOn(long targetId) throws NotFoundException, ForbiddenException {
    TravelOn target;

    target = travelOnRepository.findById(targetId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //의견이 달려있다면 삭제 거절
    if (target.getOpinionList().size() != 0) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "답변이 달린 여행On 은 삭제할 수 없습니다.");
    }

    //연관된 Plan 엔티티 해제
    target.releasePlan();

    //여행 On 삭제
    travelOnRepository.remove(target);
  }

  /**
   * 지역으로 조회, 키워드에 관계없이 조회하는 메서드
   * @param request
   * @return
   * @throws NotFoundException
   */
  private List<TravelOn> findByRegionWithoutKeyword(AllTravelOnGetRequest request) throws NotFoundException {
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
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 Region ID 입니다.")
    );

    if (withOpinions == null) {
      result = travelOnRepository.findAllByRegion(region, lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinionByRegion(region, lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinionByRegion(region, lastItemId, size, sortBy);
    }

    return result;
  }

  /**
   * 지역과 키워드에 관계없이 조회하는 메서드
   * @param request
   * @return
   */
  private List<TravelOn> findWithoutRegionAndKeyword(AllTravelOnGetRequest request) {
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

    if (withOpinions == null) {
      result = travelOnRepository.findAll(lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinion(lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinion(lastItemId, size, sortBy);
    }

    return result;
  }

  /**
   * 지역에 관계없이 조회, 키워드로 조회하는 메서드
   * @param request
   * @return
   */
  private List<TravelOn> findByKeywordWithoutRegion(AllTravelOnGetRequest request) {
    List<TravelOn> result;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    Long lastItemId;
    int size;
    String keyword;

    //초기화
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();
    keyword = request.getKeyword();

    if (withOpinions == null) {
      result = travelOnRepository.findAllByKeyword(keyword, lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinionByKeyword(keyword, lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinionByKeyword(keyword, lastItemId, size, sortBy);
    }

    return result;
  }

  /**
   * 지역과 키워드로 조회하는 메서드
   * @param request
   * @return
   */
  private List<TravelOn> findByRegionAndKeyword(AllTravelOnGetRequest request) throws NotFoundException {
    List<TravelOn> result;
    Boolean withOpinions;
    TravelOnSortType sortBy;
    long regionId;
    Long lastItemId;
    int size;
    String keyword;

    //초기화
    withOpinions = request.getWithOpinions();
    sortBy = request.getSortBy();
    regionId = request.getRegionId();
    lastItemId = request.getPageRequest().getLastItemId();
    size = request.getPageRequest().getSize();
    keyword = request.getKeyword();

    Region region = regionRepository.findById(regionId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 Region ID 입니다.")
    );

    if (withOpinions == null) {
      result = travelOnRepository.findAllByRegionAndKeyword(region, keyword, lastItemId, size, sortBy);
    } else if (withOpinions) {
      result = travelOnRepository.findHasOpinionByRegionAndKeyword(region, keyword, lastItemId, size, sortBy);
    } else {
      result = travelOnRepository.findNoOpinionByRegionAndKeyword(region, keyword, lastItemId, size, sortBy);
    }

    return result;
  }

}
