package com.heylocal.traveler.util.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;

/**
 * 포맷에 맞춰 S3의 Object 이름을 생성하는 클래스
 */
@Slf4j
@Component
public class S3ObjectNameFormatter {
  private static final String TRAVEL_ON_ID_PARAM = "{여행OnId}";
  private static final String OPINION_ID_PARAM = "{답변Id}";
  private static final String IMG_TYPE_PARAM = "{이미지종류}";
  private static final String FILE_NAME_PARAM = "{파일이름}";
  private static final String OPINION_IMG_FORMAT
      = "opinions/" + TRAVEL_ON_ID_PARAM + "/" + OPINION_ID_PARAM + "/" + IMG_TYPE_PARAM + "/" + FILE_NAME_PARAM;

  /**
   * 답변 이미지를 S3에 저장할 때의 이름을 패턴에 맞춰 생성
   * @param travelOnId 답변이 달린 여행On ID
   * @param opinionId 답변 ID
   * @param imgType 이미지 종류
   * @param objectIndex 이미지 배치 순서
   * @return
   */
  public String getObjectNameOfOpinionImg(long travelOnId, long opinionId, ImageContentType imgType, long objectIndex) {
    String objectName = OPINION_IMG_FORMAT;

    objectName = objectName.replace(TRAVEL_ON_ID_PARAM, String.valueOf(travelOnId));
    objectName = objectName.replace(OPINION_ID_PARAM, String.valueOf(opinionId));
    objectName = objectName.replace(IMG_TYPE_PARAM, imgType.name());
    objectName = objectName.replace(FILE_NAME_PARAM, String.valueOf(objectIndex));

    return objectName + ".png";
  }

  /**
   * 답변 이미지 Object 이름으로 여행OnID·답변ID·배치순서 를 구하는 메서드
   * @param objectName
   * @return
   */
  public Map<ObjectNameProperty, String> parseObjectNameOfOpinionImg(String objectName) {
    Map<ObjectNameProperty, String> result = new ConcurrentHashMap<>();
    String[] splitObjectName = objectName.split("/");

    result.put(ObjectNameProperty.TRAVEL_ON_ID, splitObjectName[1]);
    result.put(ObjectNameProperty.OPINION_ID, splitObjectName[2]);
    result.put(ObjectNameProperty.IMG_TYPE, splitObjectName[3]);
    result.put(ObjectNameProperty.OBJECT_INDEX, splitObjectName[4].replace(".png", ""));

    return result;
  }

  /**
   * Object 이름을 구성할 때 사용되는 요소
   */
  public enum ObjectNameProperty {
    TRAVEL_ON_ID, OPINION_ID, IMG_TYPE, OBJECT_INDEX
  }
}
