package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.travelon.opinion.CoffeeType;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.OpinionImageContentDto;
import com.heylocal.traveler.dto.PlaceDto;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.OpinionImageContentRepository;
import com.heylocal.traveler.repository.OpinionRepository;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.ObjectNameProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;

class OpinionImgContentServiceTest {
  @Mock
  private S3ClientService s3ClientService;
  @Mock
  private OpinionImageContentRepository opinionImageContentRepository;
  @Mock
  private OpinionRepository opinionRepository;
  @Mock
  private S3ObjectNameFormatter s3ObjectNameFormatter;
  @Mock
  private S3PresignUrlProvider s3PresignUrlProvider;
  private OpinionImgContentService opinionImgContentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    opinionImgContentService = new OpinionImgContentService(s3ClientService, opinionImageContentRepository, opinionRepository, s3ObjectNameFormatter, s3PresignUrlProvider);
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

  @Test
  @DisplayName("해당 답변의 모든 이미지 엔티티 id 조회")
  void inquiryOpinionImgContentIdsTest() throws NotFoundException {
    //GIVEN
    long imgEntity1Id = 1L;
    OpinionImageContent imgEntity1 = OpinionImageContent.builder()
        .id(imgEntity1Id)
        .imageContentType(ImageContentType.GENERAL)
        .objectKeyName("objecy key 1")
        .build();
    long imgEntity2Id = 2L;
    OpinionImageContent imgEntity2 = OpinionImageContent.builder()
        .id(imgEntity2Id)
        .imageContentType(ImageContentType.RECOMMEND_FOOD)
        .objectKeyName("objecy key 2")
        .build();
    long opinionId = 3L;
    Opinion opinion = Opinion.builder()
        .id(opinionId)
        .build();
    imgEntity1.setOpinion(opinion);
    imgEntity2.setOpinion(opinion);

    //Mock 행동 정의 - opinionRepository
    willReturn(Optional.of(opinion)).given(opinionRepository).findById(opinionId);

    //WHEN
    long[] imgIdsResult = opinionImgContentService.inquiryOpinionImgContentIds(opinionId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회된 이미지 엔티티의 개수가 2개인지
        () -> assertSame(2, imgIdsResult.length),
        //성공 케이스 - 2 - 조회된 이미지 엔티티 ID가 올바른지
        () -> assertSame(imgEntity1Id, imgIdsResult[0]),
        () -> assertSame(imgEntity2Id, imgIdsResult[1]),
        //실패 케이스 - 1 - 존재하지 않는 답변 ID 인 경우
        () -> assertThrows(NotFoundException.class, () -> opinionImgContentService.inquiryOpinionImgContentIds(10))
    );
  }

  @Test
  @DisplayName("업로드 Presigned URL 생성")
  void getUploadPresignedUrlTest() {
    //GIVEN
    long travelOnIdOfOpinion = 1L;
    long newOpinionId = 2L;
    PlaceDto.PlaceRequest placeRequest = getPlaceRequest(3L);
    OpinionDto.OpinionRequest opinionRequest = getOpinionRequest(placeRequest);
    int generalImgQuantity = 1;
    int foodImgQuantity = 2;
    int drinkAndDessertImgQuantity = 3;
    int photoSpotImgQuantity = 2;
    OpinionImageContentDto.ImageContentQuantity imgQuantity = OpinionImageContentDto.ImageContentQuantity.builder()
        .generalImgQuantity(generalImgQuantity)
        .foodImgQuantity(foodImgQuantity)
        .drinkAndDessertImgQuantity(drinkAndDessertImgQuantity)
        .photoSpotImgQuantity(photoSpotImgQuantity)
        .build();
    opinionRequest.setQuantity(imgQuantity);

    //WHEN
    Map<ImageContentType, List<String>> result = opinionImgContentService.getUploadPresignedUrl(imgQuantity, travelOnIdOfOpinion, newOpinionId);

    //THEN
    assertAll(
        //성공 케이스 - 업로드할 이미지의 개수(quantity)만큼 업로드용 Presigned URL 가 생성되었는지
        () -> assertSame(generalImgQuantity, result.get(ImageContentType.GENERAL).size()),
        () -> assertSame(foodImgQuantity, result.get(ImageContentType.RECOMMEND_FOOD).size()),
        () -> assertSame(drinkAndDessertImgQuantity, result.get(ImageContentType.RECOMMEND_DRINK_DESSERT).size()),
        () -> assertSame(photoSpotImgQuantity, result.get(ImageContentType.PHOTO_SPOT).size())
    );
  }

  @Test
  @DisplayName("해당 id들의 OpinionImageContent 제거")
  void removeOpinionImgContentsTest() throws NotFoundException {
    //GIVEN
    long imgEntity1Id = 1L;
    OpinionImageContent imgEntity1 = OpinionImageContent.builder()
        .id(imgEntity1Id)
        .imageContentType(ImageContentType.GENERAL)
        .objectKeyName("objecy key 1")
        .build();
    long imgEntity2Id = 2L;
    OpinionImageContent imgEntity2 = OpinionImageContent.builder()
        .id(imgEntity2Id)
        .imageContentType(ImageContentType.RECOMMEND_FOOD)
        .objectKeyName("objecy key 2")
        .build();
    long[] opinionImgContentIdAry = new long[]{imgEntity1Id, imgEntity2Id};

    //Mock 행동 정의 - opinionImageContentRepository
    willReturn(Optional.of(imgEntity1)).given(opinionImageContentRepository).findById(imgEntity1Id);
    willReturn(Optional.of(imgEntity2)).given(opinionImageContentRepository).findById(imgEntity2Id);

    //WHEN
    opinionImgContentService.removeOpinionImgContents(opinionImgContentIdAry);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 전달된 id 개수만큼 반복하여 삭제하는지
        () -> then(opinionImageContentRepository).should(times(2)).findById(anyLong()),
        () -> then(opinionImageContentRepository).should(times(2)).remove(any())
    );
  }

  private OpinionDto.OpinionRequest getOpinionRequest(PlaceDto.PlaceRequest place) {
    return OpinionDto.OpinionRequest.builder()
        .description("myDescription")
        .place(place)
        .facilityCleanliness(EvaluationDegree.GOOD)
        .costPerformance(EvaluationDegree.GOOD)
        .waiting(false)
        .coffeeType(CoffeeType.BITTER)
        .build();
  }

  private PlaceDto.PlaceRequest getPlaceRequest(long placeId) {
    return PlaceDto.PlaceRequest.builder()
        .id(placeId)
        .category(PlaceCategory.CE7)
        .name("myPlace")
        .roadAddress("myRoadAddress")
        .address("myAddress")
        .lat(10)
        .lng(10)
        .kakaoLink("myLink")
        .build();
  }
}