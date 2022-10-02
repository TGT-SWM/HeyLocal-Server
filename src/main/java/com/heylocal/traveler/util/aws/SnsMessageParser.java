package com.heylocal.traveler.util.aws;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SnsMessageParser {
  //AWS SNS 로부터 받은 답변 이미지 Object Key 에 해당하는 정규표현식
  public static final String OPINION_IMG_OBJECT_KEY_REGEX = "\\\\\"key\\\\\":\\\\\"opinions/.*png\\\\\"";
  //AWS SNS 로부터 받은 프로필 이미지 Object Key 에 해당하는 정규표현식
  public static final String PROFILE_IMG_OBJECT_KEY_REGEX = "\\\\\"key\\\\\":\\\\\"profiles/.*png\\\\\"";

  /**
   * SNS 로부터 전달받은 메시지에서 답변 이미지 S3 Object의 key 값을 추출하는 메서드
   * @param snsMessageValue
   * @return
   */
  public String getOpinionImgObjectName(String snsMessageValue) throws Exception {
    String objectName = "";
    Matcher matcher = getRegexMatcher(snsMessageValue, OPINION_IMG_OBJECT_KEY_REGEX);
    objectName = getObjectName(matcher);
    return objectName;
  }

  /**
   * SNS 로부터 전달받은 메시지에서 사용자 프로필 이미지 S3 Object의 key 값을 추출하는 메서드
   * @param snsMessageValue
   * @return
   */
  public String getProfileImgObjectName(String snsMessageValue) throws Exception {
    String objectName = "";
    Matcher matcher = getRegexMatcher(snsMessageValue, PROFILE_IMG_OBJECT_KEY_REGEX);
    objectName = getObjectName(matcher);
    return objectName;
  }

  /**
   * 정규표현식에 맞춰 Mather 객체를 생성하는 메서드
   * @param snsMessageValue raw string
   * @param objectKeyRegex 정규표현식
   * @return
   */
  private Matcher getRegexMatcher(String snsMessageValue, String objectKeyRegex) {
    Pattern pattern = Pattern.compile(objectKeyRegex);
    Matcher matcher = pattern.matcher(snsMessageValue);
    return matcher;
  }

  /**
   * 오브젝트 이름을 추출하는 메서드
   * @param matcher 추출에 사용할 정규표현식 Matcher 객체
   * @return
   * @throws Exception
   */
  private String getObjectName(Matcher matcher) throws Exception {
    String objectName;
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
