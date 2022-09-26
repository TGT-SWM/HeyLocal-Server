package com.heylocal.traveler.util.aws;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SnsMessageParser {
  //AWS SNS 로부터 받은 답변 이미지 Object Key 에 해당하는 정규표현식
  public static final String OPINION_IMG_OBJECT_KEY_REGEX = "\\\\\"key\\\\\":\\\\\"opinions/.*png\\\\\"";

  /**
   * SNS 로부터 전달받은 메시지에서 S3 Object의 key 값을 추출하는 메서드
   * @param snsMessageValue
   * @return
   */
  public String getObjectName(String snsMessageValue) throws Exception {
    String objectName = "";
    Pattern pattern = Pattern.compile(OPINION_IMG_OBJECT_KEY_REGEX);
    Matcher matcher = pattern.matcher(snsMessageValue);
    if (matcher.find()) {
      objectName = matcher.group().replaceAll("\\\\", "");
      objectName = objectName.replaceAll("\"", "");
      objectName = objectName.split(":")[1];
    } else {
      throw new Exception("Object 이름을 추출할 수 없습니다.");
    }
    return objectName;
  }
}
