package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.OpinionMapper;
import com.heylocal.traveler.mapper.PlaceMapper;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import com.heylocal.traveler.repository.UserRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.dto.OpinionDto.OpinionRequest;
import static com.heylocal.traveler.dto.OpinionDto.OpinionResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionService {

  private final RegionService regionService;
  private final UserRepository userRepository;
  private final TravelOnRepository travelOnRepository;
  private final PlaceRepository placeRepository;
  private final OpinionRepository opinionRepository;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;
  private final S3PresignUrlProvider s3PresignUrlProvider;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

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
  public Long addNewOpinion(long travelOnId, OpinionRequest request, LoginUser loginUser) throws NotFoundException, ForbiddenException, BadRequestException {
    long authorId;
    TravelOn travelOn;
    String requestPlaceAddress;
    Region regionOfRequestPlace;
    Place requestPlace;
    Opinion newOpinion;
    boolean isSameRegion;
    User opinionAuthor;

    //답변이 달릴 여행On 조회
    travelOn = inquiryTravelOn(travelOnId);

    //지역 조회
    requestPlaceAddress = request.getPlace().getAddress();
    regionOfRequestPlace = inquiryRegionByAddress(requestPlaceAddress);

    //여행On의 지역과 답변할 장소의 지역이 다르면 거부
    isSameRegion = isSameRegionOfTravelOnAndOpinion(travelOn, regionOfRequestPlace);
    if (!isSameRegion) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "여행On의 지역과 다른 지역의 장소는 등록할 수 없습니다.");
    }

    //작성자 조회
    authorId = loginUser.getId();
    opinionAuthor = userRepository.findById(authorId).get();

    //장소 저장 및 조회
    requestPlace = inquiryPlaceFromOpinionRequest(request);

    //새 답변 추가 (사진 정보 제외)
    newOpinion = OpinionMapper.INSTANCE.toEntity(request, requestPlace, opinionAuthor, travelOn, regionOfRequestPlace);
    opinionRepository.save(newOpinion);

    return newOpinion.getId();
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
    targetTravelOn = inquiryTravelOn(travelOnId);

    //List<Opinion> -> List<OpinionResponse>
    result = targetTravelOn.getOpinionList().stream().map(OpinionMapper.INSTANCE::toResponseDto).collect(Collectors.toList());

    return result;
  }

  /**
   * 답변(Opinion) 을 수정하는 메서드
   * @param travelOnId 수정할 답변이 달릴 여행On ID
   * @param opinionId 수정할 답변 ID
   * @param request 답변 수정 내용
   * @throws NotFoundException
   * @throws BadRequestException
   * @throws ForbiddenException
   */
  @Transactional
  public void updateOpinion(long travelOnId, long opinionId, OpinionRequest request) throws NotFoundException, BadRequestException, ForbiddenException {
    Opinion targetOpinion;
    TravelOn targetTravelOn;
    String requestPlaceAddress;
    Region regionOfRequestPlace;
    boolean isSameRegion;
    Place requestPlace;

    //답변이 달린 여행On 조회
    targetTravelOn = inquiryTravelOn(travelOnId);

    //수정할 답변(Opinion) 조회
    targetOpinion = inquiryOpinionOfTravelOn(travelOnId, opinionId);

    //수정 답변 장소의 지역 조회
    requestPlaceAddress = request.getPlace().getAddress();
    regionOfRequestPlace = inquiryRegionByAddress(requestPlaceAddress);

    //여행On의 지역과 답변할 장소의 지역이 다르면 거부
    isSameRegion = isSameRegionOfTravelOnAndOpinion(targetTravelOn, regionOfRequestPlace);
    if (!isSameRegion) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "여행On의 지역과 다른 지역의 장소는 등록할 수 없습니다.");
    }

    //Opinion Request 의 Place 를 조회
    requestPlace = inquiryPlaceFromOpinionRequest(request);

    // TODO - S3에 저장된 기존 Img 파일 제거 로직 추가

    //답변 수정
    OpinionMapper.INSTANCE.updateOpinion(request, regionOfRequestPlace, requestPlace, targetOpinion);

  }

  /**
   * 해당 여행On의 답변을 삭제하는 메서드
   * @param travelOnId 삭제할 답변이 달린 여행On ID
   * @param opinionId 삭제할 답변 ID
   */
  @Transactional
  public void removeOpinion(long travelOnId, long opinionId) throws NotFoundException {
    Opinion targetOpinion;

    //수정할 답변(Opinion) 조회
    targetOpinion = inquiryOpinionOfTravelOn(travelOnId, opinionId);

    //S3에서 답변 관련 이미지 제거
    removeOpinionImgInS3(targetOpinion.getOpinionImageContentList());

    opinionRepository.remove(targetOpinion);
  }

  /**
   * 해당 답변(opinion)의 작성자인지 확인하는 메서드
   * @param userId
   * @param opinionId
   * @return true:작성자맞음, false:작성자아님
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public boolean isAuthor(long userId, long opinionId) throws NotFoundException {
    Optional<Opinion> opinionOptional;
    Opinion opinion;

    opinionOptional = opinionRepository.findById(opinionId);
    if (opinionOptional.isEmpty()) {
      throw new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 답변 ID 입니다.");
    }
    opinion = opinionOptional.get();

    return opinion.getAuthor().getId() == userId;
  }

  /**
   * OpinionImageType 에 따라, 각각의 Presigned URL 을 생성하는 메서드
   * @param request
   * @param travelOnId
   * @param newOpinionId
   * @return
   */
  public Map<String, List<String>> getPresignedUrl(OpinionRequest request, long travelOnId, long newOpinionId) {
    int generalImgQuantity = request.getGeneralImgQuantity();
    int foodImgQuantity = request.getFoodImgQuantity();
    int drinkAndDessertImgQuantity = request.getDrinkAndDessertImgQuantity();
    int photoSpotImgQuantity = request.getPhotoSpotImgQuantity();
    Map<String, List<String>> result = new ConcurrentHashMap<>();

    //ImageContentType 마다 반복
    for (ImageContentType type : ImageContentType.values()) { //for 문 시작
      ArrayList<String> urls = new ArrayList<>();
      int quantity = 0;

      if (type == ImageContentType.GENERAL) quantity = generalImgQuantity;
      else if (type == ImageContentType.RECOMMEND_FOOD) quantity = foodImgQuantity;
      else if (type == ImageContentType.RECOMMEND_DRINK_DESSERT) quantity = drinkAndDessertImgQuantity;
      else if (type == ImageContentType.PHOTO_SPOT) quantity = photoSpotImgQuantity;

      while (quantity > 0) { //while 문 시작
        quantity--;
        String objectNameOfOpinionImg =
            s3ObjectNameFormatter.getObjectNameOfOpinionImg(travelOnId, newOpinionId, type, quantity);
        String presignedUrl = s3PresignUrlProvider.getUploadUrl(objectNameOfOpinionImg);

        urls.add(presignedUrl);
      } //while 문 끝

      result.put(type.name(), urls);
    } //for 문 끝

    return result;
  }

  /**
   * 해당 여행On 에 달린 답변을 조회하는 메서드
   * @param travelOnId
   * @param opinionId
   * @return
   * @throws NotFoundException
   */
  private Opinion inquiryOpinionOfTravelOn(long travelOnId, long opinionId) throws NotFoundException {
    Optional<Opinion> opinionOptional;
    Opinion opinion;

    opinionOptional = opinionRepository.findByIdAndTravelOn(opinionId, travelOnId);
    if (opinionOptional.isEmpty()) {
      throw new NotFoundException(NotFoundCode.NO_INFO, "해당 여행On에 존재하지 않는 답변 ID 입니다.");
    }
    opinion = opinionOptional.get();

    return opinion;
  }

  /**
   * Opinion 에 해당되는 Place 를 구하는 메서드
   * @param request Opinion 정보
   * @return
   */
  private Place inquiryPlaceFromOpinionRequest(OpinionRequest request) throws NotFoundException, BadRequestException {
    Place place;
    long placeId;
    Optional<Place> existedPlaceOptional;
    String requestPlaceAddress;
    Region regionOfRequestPlace;

    requestPlaceAddress = request.getPlace().getAddress();
    regionOfRequestPlace = inquiryRegionByAddress(requestPlaceAddress);
    placeId = request.getPlace().getId();
    existedPlaceOptional = placeRepository.findById(placeId);

    if (existedPlaceOptional.isEmpty()) { //기존에 저장된 장소가 없다면
      place = PlaceMapper.INSTANCE.toEntity(request.getPlace(), regionOfRequestPlace);
      placeRepository.save(place);

    } else { //기존에 저장된 장소가 있다면
      updatePlace(existedPlaceOptional.get(), request.getPlace()); //장소 정보 업데이트
      place = existedPlaceOptional.get();
    }

    return place;
  }

  /**
   * 여행On의 지역과 답변 장소의 지역이 같은지 확인하는 메서드
   * @param travelOn
   * @param placeRegion
   * @return
   */
  private boolean isSameRegionOfTravelOnAndOpinion(TravelOn travelOn, Region placeRegion) {
    if (travelOn.getRegion() != placeRegion) {
      return false;
    }
    return true;
  }

  /**
   * 요청된
   * @para된 requestPlaceAddress
   * @return
   * @throws NotFoundException
   * @throws BadRequestException
   */
  private Region inquiryRegionByAddress(String requestPlaceAddress) throws NotFoundException, BadRequestException {
    return regionService.getRegionByAddress(requestPlaceAddress).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "주소 관련 Region을 찾을 수 없습니다.")
    );
  }

  /**
   * 여행On 을 ID 로 조회하는 메서드
   * @param travelOnId 조회할 여행On 의 ID
   * @return
   * @throws NotFoundException
   */
  private TravelOn inquiryTravelOn(long travelOnId) throws NotFoundException {
    return travelOnRepository.findById(travelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 여행On ID 입니다.")
    );
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
   * 해당 OpinionImageContent 를 S3에서 제거하는 메서드
   * @param opinionImageContentList 제거할 OpinionImageContent 가 담긴 리스트
   */
  private void removeOpinionImgInS3(List<OpinionImageContent> opinionImageContentList) {
    // TODO - 저장되어있던 S3의 Img을 삭제하는 로직 필요
  }

}
