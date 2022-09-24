package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.OpinionImageContentRepository;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;

class OpinionImgContentServiceTest {
  @Mock
  private OpinionImageContentRepository opinionImageContentRepository;
  @Mock
  private OpinionRepository opinionRepository;
  @Mock
  private S3ObjectNameFormatter s3ObjectNameFormatter;
  private OpinionImgContentService opinionImgContentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    opinionImgContentService = new OpinionImgContentService(opinionImageContentRepository, opinionRepository, s3ObjectNameFormatter);
  }

  @Test
  @DisplayName("OpinionImageContent 저장 - 성공 케이스")
  void saveOpinionImageContentSucceedTest() {
    //GIVEN
    String s3ObjectKey = "S3에 저장된 Object 의 Key (여행OnID, 답변ID, 이미지종류, 순서Index 로 구성됨)";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(s3ObjectKey).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    Map<ObjectNameProperty, String> parsedObjectKey = new ConcurrentHashMap<>();
    parsedObjectKey.put(ObjectNameProperty.TRAVEL_ON_ID, "1");
    parsedObjectKey.put(ObjectNameProperty.OPINION_ID, "3");
    parsedObjectKey.put(ObjectNameProperty.IMG_TYPE, ImageContentType.GENERAL.name());
    parsedObjectKey.put(ObjectNameProperty.OBJECT_INDEX, "0");
    willReturn(parsedObjectKey).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(eq(s3ObjectKey));

    //Mock 행동 정의 - opinionRepository
    Opinion opinion = Opinion.builder().build();
    willReturn(Optional.of(opinion)).given(opinionRepository).findByIdAndTravelOn(anyLong(), anyLong());

    //WHEN

    //THEN
    assertAll(
        () -> assertDoesNotThrow(() -> opinionImgContentService.saveOpinionImageContent(s3ObjectDto)),
        () -> then(opinionImageContentRepository).should(times(1)).save(any())
    );
  }

  @Test
  @DisplayName("OpinionImageContent 저장 - 잘못된 여행On, 답변 ID")
  void saveOpinionImageContentWrongIdTest() {
    //GIVEN
    String s3ObjectKey = "S3에 저장된 Object 의 Key (여행OnID, 답변ID, 이미지종류, 순서Index 로 구성됨)";
    S3ObjectDto s3ObjectDto = S3ObjectDto.builder().key(s3ObjectKey).build();

    //Mock 행동 정의 - s3ObjectNameFormatter
    Map<ObjectNameProperty, String> parsedObjectKey = new ConcurrentHashMap<>();
    parsedObjectKey.put(ObjectNameProperty.TRAVEL_ON_ID, "1");
    parsedObjectKey.put(ObjectNameProperty.OPINION_ID, "3");
    parsedObjectKey.put(ObjectNameProperty.IMG_TYPE, ImageContentType.GENERAL.name());
    parsedObjectKey.put(ObjectNameProperty.OBJECT_INDEX, "0");
    willReturn(parsedObjectKey).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(eq(s3ObjectKey));

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.empty()).given(opinionRepository).findByIdAndTravelOn(anyLong(), anyLong());

    //WHEN

    //THEN
    assertAll(
        () -> assertThrows(NotFoundException.class, () -> opinionImgContentService.saveOpinionImageContent(s3ObjectDto)),
        () -> then(opinionImageContentRepository).should(times(0)).save(any())
    );
  }
}