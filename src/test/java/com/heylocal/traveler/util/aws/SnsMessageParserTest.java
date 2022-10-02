package com.heylocal.traveler.util.aws;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnsMessageParserTest {
  private SnsMessageParser snsMessageParser = new SnsMessageParser();

  @Test
  @DisplayName("AWS SNS 가 전달해준 S3 Object 정보에서 Object Name 을 추출")
  void getObjectNameTest() throws Exception {
    //GIVEN
    String objectName = "opinions/1/7/RECOMMEND_FOOD/0.png";
    String snsMessage = getSnsMessageExample(objectName);

    //WHEN
    String result = snsMessageParser.getOpinionImgObjectName(snsMessage);

    //THEN
    assertAll(
        //성공 케이스
        () -> assertDoesNotThrow(() -> snsMessageParser.getOpinionImgObjectName(snsMessage)),
        () -> assertEquals(objectName, result)
    );
  }

  @Test
  @DisplayName("SNS 로부터 전달받은 메시지에서 사용자 프로필 이미지 Object Key(Name) 추출")
  void getProfileImgObjectNameTest() throws Exception {
    //GIVEN
    String objectName = "profiles/1/profile.png";
    String snsMessage = getSnsMessageExample(objectName);

    //WHEN
    String result = snsMessageParser.getProfileImgObjectName(snsMessage);

    //THEN
    assertEquals(objectName, result);
  }

  private String getSnsMessageExample(String objectName) {
    return "{\n" +
        "  \"Type\" : \"Notification\",\n" +
        "  \"MessageId\" : \"03ed2044-2a54-565e-a974-7be3555edc7c\",\n" +
        "  \"TopicArn\" : \"arn:aws:sns:ap-northeast-2:711322672068:heylocal-opinion-img\",\n" +
        "  \"Subject\" : \"Amazon S3 Notification\",\n" +
        "  \"Message\" : \"{\\\"Records\\\":[{\\\"eventVersion\\\":\\\"2.1\\\",\\\"eventSource\\\":\\\"aws:s3\\\",\\\"awsRegion\\\":\\\"ap-northeast-2\\\",\\\"eventTime\\\":\\\"2022-09-23T09:30:49.711Z\\\",\\\"eventName\\\":\\\"ObjectCreated:Put\\\",\\\"userIdentity\\\":{\\\"principalId\\\":\\\"AWS:AIDA2LHRCTPCOPKJFLYP5\\\"},\\\"requestParameters\\\":{\\\"sourceIPAddress\\\":\\\"106.249.151.143\\\"},\\\"responseElements\\\":{\\\"x-amz-request-id\\\":\\\"ZMBAK18H65GVZDP1\\\",\\\"x-amz-id-2\\\":\\\"BJEfp9HCQyFHuRF6w3RFX7k6I0nYB0mNtKPnfgAxqwvKT4y7QAmUz6hgVNm3bTFVZL/OgSbZTQWwKX8W3lw07Wc/AF2pSBmATI2C2BYOYo8=\\\"},\\\"s3\\\":{\\\"s3SchemaVersion\\\":\\\"1.0\\\",\\\"configurationId\\\":\\\"heylocal-opinion-img-put-sns-alert\\\",\\\"bucket\\\":{\\\"name\\\":\\\"heylocal\\\",\\\"ownerIdentity\\\":{\\\"principalId\\\":\\\"AKOEGY7YU545Z\\\"},\\\"arn\\\":\\\"arn:aws:s3:::heylocal\\\"},\\\"object\\\":{\\\"key\\\":\\\""
        + objectName + "\\\",\\\"size\\\":559359,\\\"eTag\\\":\\\"ced235bb640438c406744de7f6a026ef\\\",\\\"sequencer\\\":\\\"00632D7CC98E8B1717\\\"}}}]}\",\n" +
        "  \"Timestamp\" : \"2022-09-23T09:30:50.956Z\",\n" +
        "  \"SignatureVersion\" : \"1\",\n" +
        "  \"Signature\" : \"Z19kA3NK5nC4HTh6vsrFj9lMaHBUHfW5UwhGlcZITX+PkC+tOmjlS9sqfJyo/Tl19ub1zktuy+oXSg/aT3Q5Rjz6DghoKMLD2ZCtXNQ8h47kKs6X1/zBsOldGucaJGaGzOCKvZKfGfchFRBmwvmoqQDhK3mVMlQvbuTGJV+oeyl+jY3RwWSuOkXR4f+ZG3yz21WZbYlFF1sYYDq8VIuN7jxO96XXRoIxBvdwqgSz51UHuwEi8uz+Rsg3nOUdn/m1aScSxe7tgiFyA4TMMrjrysGo12Oula8u3JqJo35a4WlUUYyXX08UWM+q9xMlulKMDnDzpWFM5ESqiFm9ZjM1Ag==\",\n" +
        "  \"SigningCertURL\" : \"https://sns.ap-northeast-2.amazonaws.com/SimpleNotificationService-56e67fcb41f6fec09b0196692625d385.pem\",\n" +
        "  \"UnsubscribeURL\" : \"https://sns.ap-northeast-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:ap-northeast-2:711322672068:heylocal-opinion-img:b845ce97-3dfb-4e40-a1d8-565cac5e4469\"\n" +
        "}";
  }
}