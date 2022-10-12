package com.heylocal.traveler.mapper.context;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.dto.OpinionDto.OpinionWithPlaceResponse;
import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.ObjectNameProperty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;

class S3UrlOpinionContextTest {
  @Mock
  private S3UrlUserContext s3UrlUserContext;
  @Mock
  private S3ObjectNameFormatter s3ObjectNameFormatter;
  @Mock
  private S3PresignUrlProvider s3PresignUrlProvider;
  private S3UrlOpinionContext s3UrlOpinionContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    s3UrlOpinionContext = new S3UrlOpinionContext(s3UrlUserContext, s3ObjectNameFormatter, s3PresignUrlProvider);
  }

  @Test
  @DisplayName("다운로드 URL 바인딩")
  void bindS3DownloadUrlTest() {
    //GIVEN
    UserProfileResponse authorDto = UserProfileResponse.builder().build();
    OpinionWithPlaceResponse responseDto = OpinionWithPlaceResponse.builder()
        .author(authorDto)
        .build();
    User authorEntity = User.builder().build();
    long keyIndex1 = 0;
    long keyIndex2 = 1;
    String key1 = "opinions/1/2/GENERAL/" + keyIndex1 + ".png";
    String key2 = "opinions/1/2/GENERAL/" + keyIndex2 + ".png";
    Opinion opinionEntity = Opinion.builder()
        .author(authorEntity)
        .build();
    opinionEntity.addOpinionImageContent(OpinionImageContent.builder().imageContentType(OpinionImageContent.ImageContentType.GENERAL).objectKeyName(key1).build());
    opinionEntity.addOpinionImageContent(OpinionImageContent.builder().imageContentType(OpinionImageContent.ImageContentType.GENERAL).objectKeyName(key2).build());

    //Mock 행동 정의 - s3ObjectNameFormatter
    Map<ObjectNameProperty, String> keyItems1 = new ConcurrentHashMap<>();
    keyItems1.put(ObjectNameProperty.OBJECT_INDEX, String.valueOf(keyIndex1));
    Map<ObjectNameProperty, String> keyItems2 = new ConcurrentHashMap<>();
    keyItems2.put(ObjectNameProperty.OBJECT_INDEX, String.valueOf(keyIndex2));

    willReturn(keyItems1).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(key1);
    willReturn(keyItems2).given(s3ObjectNameFormatter).parseObjectNameOfOpinionImg(key2);

    //Mock 행동 정의 - s3PresignUrlProvider
    String presignedUrlOfKey1 = "presignedUrlOfKey1";
    String presignedUrlOfKey2 = "presignedUrlOfKey2";
    willReturn(presignedUrlOfKey1).given(s3PresignUrlProvider).getPresignedUrl(eq(key1), eq(HttpMethod.GET));
    willReturn(presignedUrlOfKey2).given(s3PresignUrlProvider).getPresignedUrl(eq(key2), eq(HttpMethod.GET));

    //WHEN
    s3UrlOpinionContext.bindS3DownloadUrl(responseDto, opinionEntity);

    //THEN
    assertAll(
        () -> assertEquals(presignedUrlOfKey1, responseDto.getGeneralImgDownloadImgUrl().get(0)),
        () -> assertEquals(presignedUrlOfKey2, responseDto.getGeneralImgDownloadImgUrl().get(1))
    );
  }
}