package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.AwsApi;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionImgContentService;
import com.heylocal.traveler.util.aws.SnsMessageParser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Aws")
@RestController
@RequiredArgsConstructor
public class AwsController implements AwsApi {
  private final OpinionImgContentService opinionImgContentService;
  private final SnsMessageParser snsMessageParser;

  /**
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * @param request 요청 HTTP Message의 바디 부분 데이터
   * @throws NotFoundException
   */
  @Override
  public void saveOpinionImgMessage(String request) throws Exception {
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = new S3ObjectDto();
    String objectName = snsMessageParser.getObjectName(request);
    s3ObjectDto.setKey(objectName);

    opinionImgContentService.saveOpinionImageContent(s3ObjectDto);
  }
}
