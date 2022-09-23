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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.dto.OpinionImageContentDto.OpinionImageContentRequest;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.*;

@Service
@RequiredArgsConstructor
public class OpinionImgContentService {
  private final OpinionImageContentRepository opinionImageContentRepository;
  private final OpinionRepository opinionRepository;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;

  @Transactional
  public void saveOpinionImageContent(S3ObjectDto s3ObjectDto) throws NotFoundException {
    String objectKeyName;
    Map<ObjectNameProperty, String> objectNamePropertyMap;
    ImageContentType targetImageType;
    long targetTravelOnId;
    Opinion targetOpinion;

    objectKeyName = s3ObjectDto.getKey();
    objectNamePropertyMap = s3ObjectNameFormatter.parseObjectNameOfOpinionImg(objectKeyName);
    targetImageType = Enum.valueOf(ImageContentType.class, objectNamePropertyMap.get(ObjectNameProperty.IMG_TYPE));
    targetTravelOnId = Long.parseLong(objectNamePropertyMap.get(ObjectNameProperty.TRAVEL_ON_ID));
    targetOpinion = opinionRepository.findById(targetTravelOnId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "답변 ID가 존재하지 않습니다.")
    );

    OpinionImageContent target = OpinionImageContentMapper.INSTANCE.toEntity(objectKeyName, targetImageType, targetOpinion);
    opinionImageContentRepository.save(target);
  }
}