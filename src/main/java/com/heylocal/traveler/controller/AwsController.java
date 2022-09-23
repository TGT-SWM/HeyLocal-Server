package com.heylocal.traveler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.controller.api.AwsApi;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionImgContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import static com.heylocal.traveler.dto.aws.AwsSnsDto.AwsSnsRequest;

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
  public void postSavedOpinionImgMessage(String request) throws NotFoundException, JsonProcessingException {
    //String -> AwsSnsRequest 객체
    AwsSnsRequest awsSnsRequest;
    awsSnsRequest = objectMapper.readValue(request, AwsSnsRequest.class);

    log.info("SNS 구독 URL: {}", awsSnsRequest.getSubscribeURL());
    log.info("전체 메시지: {}", request);
    opinionImgContentService.saveOpinionImageContent(awsSnsRequest.getObject());
  }
}
