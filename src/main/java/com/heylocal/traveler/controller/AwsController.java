/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : AwsController
 * author         : 우태균
 * date           : 2022/09/23
 * description    : AWS S3 관련 API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.AwsApi;
import com.heylocal.traveler.dto.aws.S3ObjectDto;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.service.OpinionImgContentService;
import com.heylocal.traveler.service.UserService;
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
  private final UserService userService;
  private final SnsMessageParser snsMessageParser;

  /**
   * <pre>
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * S3에 파일이 저장된 경우, SNS 가 호출하는 API 핸들러
   * </pre>
   * @param request 요청 HTTP Message의 바디 부분 데이터
   * @throws NotFoundException
   */
  @Override
  public void saveOpinionImgMessage(String request) throws Exception {
    log.info(request);
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = mapToOpinionImgS3ObjectDto(request);

    opinionImgContentService.saveOpinionImageContent(s3ObjectDto);
  }

  /**
   * <pre>
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * S3에서 파일이 제거된 경우, SNS 가 호출하는 API 핸들러
   * </pre>
   * @param request
   * @throws Exception
   */
  @Override
  public void deleteOpinionImgMessage(String request) throws Exception {
    log.info(request);
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = mapToOpinionImgS3ObjectDto(request);

    long targetImgEntityId = opinionImgContentService.inquiryOpinionImgContentId(s3ObjectDto.getKey());

    //DB에서 제거
    opinionImgContentService.removeImgEntityFromDb(targetImgEntityId);

    //S3에 저장된 오브젝트 이름 정렬
    opinionImgContentService.reorderObjectName(s3ObjectDto);
  }

  /**
   * <pre>
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * S3에서 파일이 저장된 경우, SNS 가 호출하는 API 핸들러
   * </pre>
   * @param request
   * @throws Exception
   */
  @Override
  public void saveProfileImgMessage(String request) throws Exception {
    log.info(request);
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = mapToProfileImgS3ObjectDto(request);

    userService.saveProfileObjectKey(s3ObjectDto);
  }

  /**
   * <pre>
   * AWS 에서 Content-Type: text/plain 으로 요청을 보내므로, 파라미터 타입을 String 으로 받아야 함.
   * S3에서 파일이 제거된 경우, SNS 가 호출하는 API 핸들러
   * </pre>
   * @param request
   * @throws Exception
   */
  @Override
  public void deleteProfileImgMessage(String request) throws Exception {
    log.info(request);
    //String -> AwsSnsRequest 객체
    S3ObjectDto s3ObjectDto = mapToProfileImgS3ObjectDto(request);

    userService.removeProfileObjectKey(s3ObjectDto);
  }

  private S3ObjectDto mapToOpinionImgS3ObjectDto(String request) throws Exception {
    S3ObjectDto s3ObjectDto = new S3ObjectDto();
    String objectName = snsMessageParser.getOpinionImgObjectName(request);
    s3ObjectDto.setKey(objectName);
    return s3ObjectDto;
  }

  private S3ObjectDto mapToProfileImgS3ObjectDto(String request) throws Exception {
    S3ObjectDto s3ObjectDto = new S3ObjectDto();
    String objectName = snsMessageParser.getProfileImgObjectName(request);
    s3ObjectDto.setKey(objectName);
    return s3ObjectDto;
  }
}
