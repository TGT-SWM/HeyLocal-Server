package com.heylocal.traveler.util.aws;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import org.springframework.stereotype.Component;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;

/**
 * 포맷에 맞춰 S3의 Object 이름을 생성하는 클래스
 */
@Component
public class S3ObjectNameFormatter {
  private static final String TRAVEL_ON_ID_PARAM = "{여행OnId}";
  private static final String OPINION_ID_PARAM = "{여행OnId}";
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

    return objectName;
  }
}
