package com.heylocal.traveler.controller;

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

  @Override
  public void postSavedOpinionImgMessage(AwsSnsRequest request) throws NotFoundException {
    log.info("SNS 구독 URL: {}", request.getSubscribeURL());
    opinionImgContentService.saveOpinionImageContent(request.getObject());
  }
}
