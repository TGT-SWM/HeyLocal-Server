/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : OpinionImgContentService
 * author         : 우태균
 * date           : 2022/09/23
 * description    : 여행On의 답변의 이미지 관련 서비스
 */

package com.heylocal.traveler.service;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.OpinionImageContentMapper;
import com.heylocal.traveler.repository.OpinionImageContentRepository;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.dto.OpinionImageContentDto.ImageContentQuantity;
import static com.heylocal.traveler.dto.aws.S3PresignedUrlDto.OpinionImgUpdateUrl;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.ObjectNameProperty;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionImgContentService {
  private final S3ClientService s3ClientService;
  private final OpinionImageContentRepository opinionImageContentRepository;
  private final OpinionRepository opinionRepository;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;
  private final S3PresignUrlProvider s3PresignUrlProvider;

  /**
   * <pre>
   * OpinionImageContent 를 저장하는 메서드
   * S3 Bucket에 저장된 Object 정보를 기반으로 저장한다.
   * </pre>
   * @param s3ObjectDto
   * @throws NotFoundException
   */
  @Transactional
  public void saveOpinionImageContent(S3ObjectDto s3ObjectDto) throws NotFoundException {
    String objectKeyName;
    Map<ObjectNameProperty, String> objectNamePropertyMap;
    ImageContentType targetImageType;
    long targetTravelOnId;
    long targetOpinionId;
    Opinion targetOpinion;

    objectKeyName = s3ObjectDto.getKey();

    objectNamePropertyMap = s3ObjectNameFormatter.parseObjectNameOfOpinionImg(objectKeyName);
    targetImageType = Enum.valueOf(ImageContentType.class, objectNamePropertyMap.get(ObjectNameProperty.IMG_TYPE));
    targetTravelOnId = Long.parseLong(objectNamePropertyMap.get(ObjectNameProperty.TRAVEL_ON_ID));
    targetOpinionId = Long.parseLong(objectNamePropertyMap.get(ObjectNameProperty.OPINION_ID));
    targetOpinion = opinionRepository.findByIdAndTravelOn(targetOpinionId, targetTravelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "해당 여행On에 이 답변 ID가 존재하지 않습니다.")
    );

    OpinionImageContent target = OpinionImageContentMapper.INSTANCE.toEntity(objectKeyName, targetImageType, targetOpinion);

    //이미 존재하는 Object Key 가 아닌 경우
    Optional<OpinionImageContent> existedImgContent = opinionImageContentRepository.findByObjectKeyName(objectKeyName);
    if ( !existedImgContent.isPresent() ) {
      opinionImageContentRepository.save(target); //저장
    }
  }

  /**
   * 해당 답변의 모든 답변 이미지 엔티티 id 를 조회하는 메서드
   * @param opinionId
   * @return
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public long[] inquiryOpinionImgContentIds(long opinionId) throws NotFoundException {
    Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 답변 id 입니다.")
    );
    List<OpinionImageContent> targetImgList = opinion.getOpinionImageContentList();

    Long[] opinionImgContentIdAry = targetImgList.stream().map((imgEntity) -> imgEntity.getId()).toArray(Long[]::new);
    long[] result = Arrays.stream(opinionImgContentIdAry).mapToLong(Long::longValue).toArray();

    return result;
  }

  @Transactional(readOnly = true)
  public long inquiryOpinionImgContentId(String objectKeyName) throws NotFoundException {
    OpinionImageContent opinionImageContent = opinionImageContentRepository.findByObjectKeyName(objectKeyName).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 오브젝트 Key 입니다.")
    );

    return opinionImageContent.getId();
  }

  /**
   * OpinionImageType 에 따라, 각각의 Presigned URL 을 생성하는 메서드
   * @param imgQuantity
   * @param travelOnId
   * @param newOpinionId
   * @return
   */
  public Map<ImageContentType, List<String>> getUploadPresignedUrl(ImageContentQuantity imgQuantity, long travelOnId, long newOpinionId) {
    int generalImgQuantity = imgQuantity.getGeneralImgQuantity();
    int foodImgQuantity = imgQuantity.getFoodImgQuantity();
    int drinkAndDessertImgQuantity = imgQuantity.getDrinkAndDessertImgQuantity();
    int photoSpotImgQuantity = imgQuantity.getPhotoSpotImgQuantity();
    Map<ImageContentType, List<String>> result = new ConcurrentHashMap<>();

    //ImageContentType 마다 반복
    for (ImageContentType type : ImageContentType.values()) { //for 문 시작
      ArrayList<String> urls = new ArrayList<>();
      int quantity = 0;
      int objectIndex = 0;

      if (type == ImageContentType.GENERAL) quantity = generalImgQuantity;
      else if (type == ImageContentType.RECOMMEND_FOOD) quantity = foodImgQuantity;
      else if (type == ImageContentType.RECOMMEND_DRINK_DESSERT) quantity = drinkAndDessertImgQuantity;
      else if (type == ImageContentType.PHOTO_SPOT) quantity = photoSpotImgQuantity;

      while (quantity > 0) { //while 문 시작
        quantity--;
        String objectNameOfOpinionImg =
            s3ObjectNameFormatter.getObjectNameOfOpinionImg(travelOnId, newOpinionId, type, objectIndex++);
        String presignedUrl = s3PresignUrlProvider.getPresignedUrl(objectNameOfOpinionImg, HttpMethod.PUT);

        urls.add(presignedUrl);
      } //while 문 끝

      result.put(type, urls);
    } //for 문 끝

    return result;
  }

  /**
   * <pre>
   * OpinionImageContent 타입마다,
   * PUT·DELETE Presigned URL을 생성하여 반환한다.
   * 각 타입별로 PUT·DELETE Presigned URL 을 3개씩 생성한다.
   * 이미 존재하는 이미지 개수에 관계없이, 무조건 3개의 PUT URL 을 생성한다.
   * DELETE URL 은 이미 존재하는 이미지 개수에 맞춰 생성한다.
   *
   * - 이미 존재하는 object key 에 대한 PUT URL -> 이미지 수정용 URL
   * - 존재하지 않는 object key 에 대한 PUT URL -> 이미지 추가용 URL
   * - 이미 존재하는 object key 에 대한 DELETE URL -> 이미지 삭제용 URL
   * - 존재하지 않는 object key 에 대한 DELETE URL -> 생성하지 않음
   * </pre>
   * @param opinionId 이미지 Presigned URL 을 생성할 Target 답변
   * @return
   */
  @Transactional(readOnly = true)
  public List<OpinionImgUpdateUrl> getUpdatePresignedUrl(long opinionId) throws NotFoundException {
    //업데이트할 답변 엔티티
    Opinion targetOpinion = opinionRepository.findById(opinionId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 답변 ID 입니다.")
    );
    //업데이트할 답변 이미지 엔티티들의 key들을 리스트에 담는다.
    List<String> savedObjectKeyList = targetOpinion.getOpinionImageContentList().stream()
        .map(OpinionImageContent::getObjectKeyName).collect(Collectors.toList());
    //Return 할 값
    List<OpinionImgUpdateUrl> result = new ArrayList<>();

    //이미지 타입별로 반복
    for (ImageContentType imgType : ImageContentType.values()) { //첫번째 for 문 - 시작
      //imgType 에 해당하는 PUT·DELETE URL 들을 담고있는 객체
      OpinionImgUpdateUrl updateUrl = new OpinionImgUpdateUrl();
      updateUrl.setImgType(imgType);

      //이미지 타입별로 최대 3개의 이미지를 가질 수 있으므로 총 3번 반복
      for (int objectIndex = 0; objectIndex < 3; objectIndex++) { //두번째 for 문 - 시작
        //targetOpinion 가 가질 수 있는 '답변 이미지 object key' 구하기
        String objectNameOfOpinionImg =
            s3ObjectNameFormatter.getObjectNameOfOpinionImg(targetOpinion.getTravelOn().getId(), opinionId, imgType, objectIndex);

        //존재하는 ObjectKey 인지 확인
        boolean isExistedKey = savedObjectKeyList.contains(objectNameOfOpinionImg);

        //PUT URL 생성
        String putUrl = s3PresignUrlProvider.getPresignedUrl(objectNameOfOpinionImg, HttpMethod.PUT);
        if (isExistedKey) { /* 이미 존재하는 Object Key Name 이라면, updatePutUrls 필드에 담는다. */
          updateUrl.getUpdatePutUrls().add(putUrl);
        } else { /* 존재하지 않는 Object Key Name 이라면, newPutUrls 필드에 담는다. */
          updateUrl.getNewPutUrls().add(putUrl);
        }

        //해당 key 를 갖는 OpinionImageContent 엔티티가 없다면 아래 생략
        if (!isExistedKey) continue;

        //DELETE URL 생성
        String deleteUrl = s3PresignUrlProvider.getPresignedUrl(objectNameOfOpinionImg, HttpMethod.DELETE);
        updateUrl.getDeleteUrls().add(deleteUrl);
      } //두번째 for 문 - 끝

      result.add(updateUrl);
    } //첫번째 for 문 - 끝

    return result;
  }

  /**
   * 해당 id의 OpinionImageContent 엔티티와 S3 오브젝트를 제거하는 메서드
   * @param targetIdAry
   */
  @Transactional
  public void removeOpinionImgContents(long[] targetIdAry) {
    for (long id : targetIdAry) { //for문 시작
      Optional<OpinionImageContent> imgOptional = opinionImageContentRepository.findById(id);
      if (imgOptional.isEmpty()) continue; //만약 id가 존재하지 않는다면 무시

      OpinionImageContent imgEntity = imgOptional.get();
      //S3에서 오브젝트 제거
      s3ClientService.removeObject(imgEntity.getObjectKeyName());
      //DB에서 엔티티 제거
      opinionImageContentRepository.remove(imgEntity);
    }//for문 끝
  }

  /**
   * DB 에서 OpinionImageContent 엔티티를 제거하는 메서드
   * @param imgEntityId
   * @throws NotFoundException
   */
  @Transactional
  public void removeImgEntityFromDb(long imgEntityId) throws NotFoundException {
    OpinionImageContent imgEntity = opinionImageContentRepository.findById(imgEntityId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 OpinionImageContent ID 입니다.")
    );
    opinionImageContentRepository.remove(imgEntity);
  }
}
