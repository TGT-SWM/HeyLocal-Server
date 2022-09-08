package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.heylocal.traveler.dto.OpinionDto.OpinionRequest;

@Service
@RequiredArgsConstructor
public class OpinionService {

  private final UserRepository userRepository;
  private final TravelOnRepository travelOnRepository;
  private final RegionRepository regionRepository;
  private final PlaceRepository placeRepository;
  private final OpinionRepository opinionRepository;

  /**
   * 새 답변(Opinion) 등록
   * @param travelOnId 답변이 등록될 여행On ID
   * @param request 답변 내용
   * @param loginUser 로그인한 유저 (답변 작성자)
   * @throws BadArgumentException
   */
  @Transactional
  public void addNewOpinion(long travelOnId, OpinionRequest request, LoginUser loginUser) throws BadArgumentException {
    long authorId;
    long placeId;
    TravelOn travelOn;
    Region region;
    Place place;
    Optional<Place> existedPlaceOptional;
    Opinion newOpinion;

    //답변이 달릴 여행On 조회
    travelOn = travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );

    //지역 조회
    region = getRegionByKeyword(request.getPlace().getAddress()).orElseThrow(
        () -> new BadArgumentException(NotFoundCode.NO_INFO, "주소 관련 Region을 찾을 수 없습니다.")
    );

    //작성자 조회
    authorId = loginUser.getId();
    User author = userRepository.findById(authorId).get();

    //장소 저장 및 조회
    placeId = request.getPlace().getId();
    existedPlaceOptional = placeRepository.findById(placeId);
    if (existedPlaceOptional.isEmpty()) { //기존에 저장된 장소가 없다면
      place = request.getPlace().toEntity(region);
      placeRepository.save(place);

    } else { //기존에 저장된 장소가 있다면
      updatePlace(existedPlaceOptional.get(), request.getPlace()); //장소 정보 업데이트
      place = existedPlaceOptional.get();
    }

    //새 답변 추가
    newOpinion = request.toEntity(place, author, travelOn, region);
    opinionRepository.save(newOpinion);
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

  /**
   * 주소와 Region 엔티티를 매핑해서 Region 엔티티를 반환하는 메서드
   * @param address 매핑할 주소
   * @return 매핑된 Region 엔티티
   */
  private Optional<Region> getRegionByKeyword(String address) {
    String keyword;
    String[] addressAry;
    String state;
    String city;

    addressAry = address.split(" ");
    state = addressAry[0];
    city = addressAry[1];

    if (state.contains("제주")) { //제주인 경우
      return regionRepository.findByStateKeyword("제주");

    } else if (city.endsWith("시")) { //city 가 "시"인 경우
      keyword = city.replace("시", "");
      return regionRepository.findByCityKeyword(keyword);

    } else if (city.endsWith("군")) { //city 가 "군"인 경우
      keyword = city.replace("군", "");
      return regionRepository.findByCityKeyword(keyword);

    } else { //특별시나 광역시인 경우
      keyword = state.replace("시", "");
      return regionRepository.findByStateKeyword(keyword);
    }

  }

}
