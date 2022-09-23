package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.AwsApi;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionImgContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.heylocal.traveler.dto.aws.AwsSnsDto.AwsSnsRequest;

@Tag(name = "Aws")
@RestController
@RequiredArgsConstructor
public class AwsController implements AwsApi {
  private final OpinionImgContentService opinionImgContentService;

  @Override
  public void postSavedOpinionImgMessage(AwsSnsRequest request) throws NotFoundException {
    opinionImgContentService.saveOpinionImageContent(request.getObject());
  }
}
