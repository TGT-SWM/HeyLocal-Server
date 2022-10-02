package com.heylocal.traveler.controller;

import com.heylocal.traveler.service.OpinionImgContentService;
import com.heylocal.traveler.util.aws.SnsMessageParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

class AwsControllerTest {
  @Mock
  private OpinionImgContentService opinionImgContentService;
  @Mock
  private SnsMessageParser snsMessageParser;
  private AwsController awsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    awsController = new AwsController(opinionImgContentService, snsMessageParser);
  }

  @Test
  @DisplayName("AWS SNS 가 답변 이미지 등록 요청을 보내는 Callback 핸들러")
  void saveOpinionImgMessageTest() throws Exception {
    //GIVEN
    String requestMessage = "SNS 가 보내는 S3 Bucket Object 정보";
    String wrongRequestMessage = "\"Wrong Object Data\"";

    //Mock 행동 정의 - snsMessageParser
    String objectName = "/opinions/3/2/GENERAL/0.png";
    willReturn(objectName).given(snsMessageParser).getOpinionImgObjectName(eq(requestMessage));
    willThrow(Exception.class).given(snsMessageParser).getOpinionImgObjectName(eq(wrongRequestMessage));

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1
        () -> assertDoesNotThrow(() -> awsController.saveOpinionImgMessage(requestMessage)),
        //실패 케이스 - 1 - S3 오브젝트 이름이 잘못되었을 때
        () -> assertThrows(Exception.class, () -> awsController.saveOpinionImgMessage(wrongRequestMessage))
    );

  }

  @Test
  @DisplayName("AWS SNS 가 답변 이미지 삭제 요청을 보내는 Callback 핸들러")
  void deleteOpinionImgMessageTest() throws Exception {
    //GIVEN
    String requestMessage = "SNS 가 보내는 S3 Bucket Object 정보";
    String wrongRequestMessage = "\"Wrong Object Data\"";

    //Mock 행동 정의 - snsMessageParser
    String objectName = "/opinions/3/2/GENERAL/0.png";
    willReturn(objectName).given(snsMessageParser).getOpinionImgObjectName(eq(requestMessage));
    willThrow(Exception.class).given(snsMessageParser).getOpinionImgObjectName(eq(wrongRequestMessage));

    //WHEN

    //THEN
    assertDoesNotThrow(() -> awsController.deleteOpinionImgMessage(requestMessage));
  }

  // TODO - saveProfileImgMessage
  // TODO - deleteProfileImgMessage
}