package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.OpinionDto.*;
import static com.heylocal.traveler.dto.OpinionDto.OpinionRequest;

@Service
@RequiredArgsConstructor
public class OpinionService {

  private final RegionService regionService;
  private final UserRepository userRepository;
  private final TravelOnRepository travelOnRepository;
  private final PlaceRepository placeRepository;
  private final OpinionRepository opinionRepository;

  /**
   * 새 답변(Opinion) 등록
   * @param travelOnId 답변이 등록될 여행On ID
   * @param request 답변 내용
   * @param loginUser 로그인한 유저 (답변 작성자)
   * @throws NotFoundException
   * @throws ForbiddenException
   * @throws BadRequestException
   */
  @Transactional
  public void addNewOpinion(long travelOnId, OpinionRequest request, LoginUser loginUser) throws NotFoundException, ForbiddenException, BadRequestException {
    long authorId;
    long placeId;
    TravelOn travelOn;
    Region placeRegion;
    Place place;
    Optional<Place> existedPlaceOptional;
    Opinion newOpinion;

    //답변이 달릴 여행On 조회
    travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //지역 조회
    placeRegion = regionService.getRegionByAddress(request.getPlace().getAddress()).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "주소 관련 Region을 찾을 수 없습니다.")
    );

    //여행On의 지역과 답변할 장소의 지역이 다르면 거부
    if (travelOn.getRegion() != placeRegion) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "여행On의 지역과 다른 지역의 장소는 등록할 수 없습니다.");
    }

    //작성자 조회
    authorId = loginUser.getId();
    User author = userRepository.findById(authorId).get();

    //장소 저장 및 조회
    placeId = request.getPlace().getId();
    existedPlaceOptional = placeRepository.findById(placeId);
    if (existedPlaceOptional.isEmpty()) { //기존에 저장된 장소가 없다면
      place = request.getPlace().toEntity(placeRegion);
      placeRepository.save(place);

    } else { //기존에 저장된 장소가 있다면
      updatePlace(existedPlaceOptional.get(), request.getPlace()); //장소 정보 업데이트
      place = existedPlaceOptional.get();
    }

    //새 답변 추가
    newOpinion = request.toEntity(place, author, travelOn, placeRegion);
    opinionRepository.save(newOpinion);
  }

  /**
   * 특정 여행On 의 모든 답변 조회
   * @param travelOnId 답변을 조회할 여행On의 ID
   * @return
   * @throws NotFoundException 여행On ID 가 존재하지 않을 경우
   */
  @Transactional(readOnly = true)
  public List<OpinionResponse> inquiryOpinions(long travelOnId) throws NotFoundException {
    TravelOn targetTravelOn;
    List<OpinionResponse> result;

    //답변을 조회할 여행On 조회
    targetTravelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //List<Opinion> -> List<OpinionResponse>
    result = targetTravelOn.getOpinionList().stream().map(OpinionResponse::new).collect(Collectors.toList());

    return result;
  }

  /**
   * 장소 업데이트
   * @param savedPlace 기존에 저장되었던 장소 엔티티
   * @param newPlaceInfo 해당 장소의 새 정보
   */
  private void updatePlace(Place savedPlace, PlaceDto.PlaceRequest newPlaceInfo) {
    savedPlace.updateCategory(newPlaceInfo.getCategory());
    savedPlace.updateName(newPlaceInfo.getName());
    savedPlace.updateRoadAddress(newPlaceInfo.getRoadAddress());
    savedPlace.updateAddress(newPlaceInfo.getAddress());
    savedPlace.updateCoordinates(newPlaceInfo.getLat(), newPlaceInfo.getLng());
    savedPlace.updateThumbnailUrl(newPlaceInfo.getThumbnailUrl());
    savedPlace.updateLink(newPlaceInfo.getKakaoLink());
  }

}
