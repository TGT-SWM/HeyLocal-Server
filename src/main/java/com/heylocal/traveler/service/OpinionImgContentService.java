package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.OpinionImageContentMapper;
import com.heylocal.traveler.repository.OpinionImageContentRepository;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.ObjectNameProperty;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionImgContentService {
  private final S3ClientService s3ClientService;
  private final OpinionImageContentRepository opinionImageContentRepository;
  private final OpinionRepository opinionRepository;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;

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

  /**
   * 해당 id의 OpinionImageContent 엔티티와 S3 오브젝트를 제거하는 메서드
   * @param targetIdAry
   * @throws NotFoundException
   */
  @Transactional
  public void removeOpinionImgContents(long[] targetIdAry) throws NotFoundException {
    for (long id : targetIdAry) { //for문 시작
      OpinionImageContent imgEntity = opinionImageContentRepository.findById(id).orElseThrow(
          () -> new NotFoundException(NotFoundCode.NO_INFO, "해당 id를 갖는 답변 이미지 엔티티가 없습니다.")
      );
      //S3에서 오브젝트(이미지) 제거
      String objectKey = imgEntity.getObjectKeyName();
      removeOpinionImageFromS3(objectKey);
      //DB에서 엔티티 제거
      opinionImageContentRepository.remove(imgEntity);
    }//for문 끝
  }

  /**
   * 해당 Key 를 갖는 오브젝트를 S3에서 제거하는 메서드
   * @param objectKey
   */
  private void removeOpinionImageFromS3(String objectKey) {
    s3ClientService.removeObject(objectKey);
  }
}
