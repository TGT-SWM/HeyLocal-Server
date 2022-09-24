package com.heylocal.traveler.util.aws;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import org.apache.tomcat.util.file.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.regex.Pattern;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;
import static com.heylocal.traveler.util.aws.S3ObjectNameFormatter.*;
import static org.junit.jupiter.api.Assertions.*;

class S3ObjectNameFormatterTest {
  private S3ObjectNameFormatter s3ObjectNameFormatter = new S3ObjectNameFormatter();

  @Test
  @DisplayName("답변 이미지 S3 Object Key 생성")
  void getObjectNameOfOpinionImgTest() {
    //GIVEN
    long travelOnId = 1L;
    long opinionId = 2L;
    ImageContentType imgType = ImageContentType.GENERAL;
    int objectIndex = 0;

    //WHEN
    String result = s3ObjectNameFormatter.getObjectNameOfOpinionImg(travelOnId, opinionId, imgType, objectIndex);

    //THEN
    String opinionImgFormatRegex = "^opinions\\/[0-9]*\\/[0-9]*\\/GENERAL\\/[0-9]*.png$";
    assertTrue(Pattern.matches(opinionImgFormatRegex, result));
  }

  @Test
  @DisplayName("생성한 답변 이미지 Object Key 를 다시 파싱")
  void parseObjectNameOfOpinionImgTest() {
    //GIVEN
    long travelOnId = 1L;
    long opinionId = 2L;
    ImageContentType imgType = ImageContentType.GENERAL;
    int objectIndex = 0;
    String objectKey = s3ObjectNameFormatter.getObjectNameOfOpinionImg(travelOnId, opinionId, imgType, objectIndex);

    //WHEN
    Map<ObjectNameProperty, String> resultMap = s3ObjectNameFormatter.parseObjectNameOfOpinionImg(objectKey);

    //THEN
    assertAll(
        () -> assertEquals(String.valueOf(travelOnId), resultMap.get(ObjectNameProperty.TRAVEL_ON_ID)),
        () -> assertEquals(String.valueOf(opinionId), resultMap.get(ObjectNameProperty.OPINION_ID)),
        () -> assertEquals(imgType.name(), resultMap.get(ObjectNameProperty.IMG_TYPE)),
        () -> assertEquals(String.valueOf(objectIndex), resultMap.get(ObjectNameProperty.OBJECT_INDEX))
    );
  }
}