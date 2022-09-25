package com.heylocal.traveler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.controller.api.AwsApi;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionImgContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Tag(name = "Aws")
@RestController
@RequiredArgsConstructor
public class AwsController implements AwsApi {
  private final OpinionImgContentService opinionImgContentService;
  private final ObjectMapper objectMapper;

  /**
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * @param request 요청 HTTP Message의 바디 부분 데이터
   * @throws NotFoundException
   * @throws JsonProcessingException
   */
  @Override
  public void postSavedOpinionImgMessage(String request) throws NotFoundException {
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = new S3ObjectDto();
    String objectName = getObjectName(request);
    s3ObjectDto.setKey(objectName);

    opinionImgContentService.saveOpinionImageContent(s3ObjectDto);
  }

  /**
   * SNS 로부터 전달받은 메시지에서 S3 Object의 key 값을 추출하는 메서드
   * @param request
   * @return
   */
  private String getObjectName(String request) {
    String objectName = "";
    Pattern pattern = Pattern.compile("\\\\\"key\\\\\":\\\\\"opinions/.*png\\\\\"");
    Matcher matcher = pattern.matcher(request);
    if (matcher.find()) {
      objectName = matcher.group().replaceAll("\\\\", "");
      objectName = objectName.replaceAll("\"", "");
      objectName = objectName.split(":")[1];
    }
    return objectName;
  }
}
