/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : TravelOnsController
 * author         : 우태균
 * date           : 2022/08/30
 * description    : 여행 On API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.TravelOnsApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.PlanDto.PlanResponse;
import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.service.OpinionImgContentService;
import com.heylocal.traveler.service.OpinionService;
import com.heylocal.traveler.service.RegionService;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;
import static com.heylocal.traveler.dto.OpinionDto.*;
import static com.heylocal.traveler.dto.RegionDto.*;
import static com.heylocal.traveler.dto.TravelOnDto.*;
import static com.heylocal.traveler.dto.aws.S3PresignedUrlDto.OpinionImgUpdateUrl;

@Slf4j
@Tag(name = "TravelOns")
@RestController
@RequiredArgsConstructor
public class TravelOnsController implements TravelOnsApi {
  private final BindingErrorMessageProvider errorMessageProvider;

  private final TravelOnService travelOnService;
  private final OpinionService opinionService;
  private final OpinionImgContentService opinionImgContentService;
  private final RegionService regionService;

  /**
   * 여행On 목록 조회 핸들러
   * @param request 조회 조건
   * @return
   * @throws NotFoundException Request 한 Region 이 존재하지 않는 경우
   */
  @Override
  public List<TravelOnSimpleResponse> getTravelOns(AllTravelOnGetRequest request) throws NotFoundException {
    List<TravelOnSimpleResponse> response;

    response = travelOnService.inquirySimpleTravelOns(request);

    return response;
  }

  /**
   * 여행On 등록 핸들러
   * @param request 등록할 여행On 정보
   * @param bindingResult
   * @param loginUser
   * @throws BadRequestException Input 데이터 형식이 올바르지 않은 경우
   * @throws NotFoundException Request 한 Region 이 존재하지 않는 경우
   */
  @Override
  public void createTravelOn(TravelOnRequest request,
                             BindingResult bindingResult,
                             LoginUser loginUser) throws BadRequestException, NotFoundException {
    if (bindingResult.hasFieldErrors()) {
      String errMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
    }

    travelOnService.addNewTravelOn(request, loginUser);
  }

  /**
   * 여행 On 상세 조회 핸들러
   * @param travelOnId 조회할 여행 On 의 ID
   * @return
   * @throws NotFoundException 잘못된 ID 인 경우
   */
  @Override
  public TravelOnResponse getTravelOn(long travelOnId) throws NotFoundException {
    TravelOnResponse response;

    response = travelOnService.inquiryTravelOn(travelOnId);

    return response;
  }

  /**
   * @param travelOnId 여행 On ID
   * @param loginUser 로그인 사용자 정보
   * @return 해당 여행 On으로 생성된 플랜
   * @throws ForbiddenException 조회 권한이 없는 경우
   * @throws NotFoundException 해당 ID의 여행 On이 존재하지 않는 경우
   */
  @Override
  public PlanResponse getPlanOfTravelOn(
          long travelOnId,
          LoginUser loginUser
  ) throws ForbiddenException, NotFoundException {
    long userId = loginUser.getId();
    return travelOnService.inquiryRelatedPlan(travelOnId, userId);
  }

  /**
   * 여행On 수정 핸들러
   * @param travelOnId 수정할 여행On ID
   * @param request 수정 정보
   * @param bindingResult
   * @param loginUser
   * @throws BadRequestException
   * @throws NotFoundException
   * @throws ForbiddenException
   */
  @Override
  public void updateTravelOn(long travelOnId,
                             TravelOnRequest request,
                             BindingResult bindingResult,
                             LoginUser loginUser) throws BadRequestException, NotFoundException, ForbiddenException {
    if (bindingResult.hasFieldErrors()) {
      String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
    }

    //수정 권한 확인
    isTravelOnAuthor(travelOnId, loginUser);

    //수정
    travelOnService.updateTravelOn(request, travelOnId);
  }

  /**
   * 여행On 제거 핸들러
   * @param travelOnId
   * @param loginUser
   * @throws ForbiddenException
   * @throws NotFoundException
   */
  @Override
  public void deleteTravelOn(long travelOnId, LoginUser loginUser) throws ForbiddenException, NotFoundException {
    //삭제 권한 확인
    isTravelOnAuthor(travelOnId, loginUser);

    //삭제
    travelOnService.removeTravelOn(travelOnId);
  }

  /**
   * 답변 조회 핸들러
   * @param travelOnId 답변을 조회할 여행On ID
   * @return
   * @throws NotFoundException 여행On ID가 존재하지 않을 경우
   */
  @Override
  public List<OpinionWithPlaceResponse> getOpinions(long travelOnId) throws NotFoundException {
    List<OpinionWithPlaceResponse> response = opinionService.inquiryOpinionsByUserId(travelOnId);

    return response;
  }

  /**
   * 답변(Opinion) 등록 핸들러
   * @param travelOnId
   * @param request
   * @return
   * @throws BadRequestException
   * @throws NotFoundException
   * @throws ForbiddenException
   */
  @Override
  public Map<ImageContentType, List<String>> createOpinions(long travelOnId, NewOpinionRequestRequest request, BindingResult bindingResult, LoginUser loginUser) throws BadRequestException, NotFoundException, ForbiddenException {
    long newOpinionId;

    if (bindingResult.hasFieldErrors()) {
      String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
    }

    //답변 작성
    newOpinionId = opinionService.addNewOpinion(travelOnId, request, loginUser);

    return opinionImgContentService.getUploadPresignedUrl(request.getQuantity(), travelOnId, newOpinionId);
  }

  /**
   * 답변(Opinion) 수정 핸들러
   *
   * @param travelOnId    답변이 달린 여행On ID
   * @param opinionId     수정할 답변(Opinion) ID
   * @param request       수정 내용
   * @param bindingResult
   * @param loginUser
   * @return
   */
  @Override
  public List<OpinionImgUpdateUrl> updateOpinion(long travelOnId, long opinionId,
                                                 OpinionOnlyTextRequest request, BindingResult bindingResult,
                                                 LoginUser loginUser) throws BadRequestException, NotFoundException, ForbiddenException {
    if (bindingResult.hasFieldErrors()) {
      String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
    }

    //수정 권한 확인
    isOpinionAuthor(opinionId, loginUser);

    //답변 엔티티 (text) 수정
    opinionService.updateOpinion(travelOnId, opinionId, request);

    //답변 이미지를 수정할 수 있는 Presigned URL 응답
    return opinionImgContentService.getUpdatePresignedUrl(opinionId);
  }

  /**
   * 해당 여행On 의 답변(Opinion) 삭제 핸들러
   * @param travelOnId 삭제할 답변이 달린 여행On ID
   * @param opinionId 삭제할 답변 ID
   * @param loginUser
   * @return
   */
  @Override
  public void deleteOpinion(long travelOnId, long opinionId, LoginUser loginUser) throws NotFoundException, ForbiddenException {
    //삭제 권한 확인
    isOpinionAuthor(opinionId, loginUser);

    //관련 답변 이미지 id 조회
    long[] opinionImgContentIdAry = opinionImgContentService.inquiryOpinionImgContentIds(opinionId);

    //답변 이미지 엔티티 삭제
    opinionImgContentService.removeOpinionImgContents(opinionImgContentIdAry);

    //답변 엔티티 삭제
    opinionService.removeOpinion(travelOnId, opinionId);
  }

  /**
   * 해당 여행On의 지역과 동일한 지역의 주소인지 확인하는 핸들러
   * @param travelOnId    비교할 여행On Id
   * @param targetAddress 확인할 주소
   * @return
   * @throws NotFoundException
   */
  @Override
  public RegionAddressCheckResponse checkAddressRegionWithTravelOn(long travelOnId, String targetAddress) throws NotFoundException, BadRequestException {
    TravelOnResponse travelOn = travelOnService.inquiryTravelOn(travelOnId);
    return regionService.checkAddressAsRegion(targetAddress, travelOn.getRegion().getId());
  }

  /**
   * 해당 여행On의 작성자인지 확인
   * @param travelOnId 확인할 여행On ID
   * @param loginUser 로그인한 사용자 정보
   * @throws NotFoundException
   * @throws ForbiddenException
   */
  private void isTravelOnAuthor(long travelOnId, LoginUser loginUser) throws NotFoundException, ForbiddenException {
    boolean isAuthor = false;

    isAuthor = travelOnService.isAuthor(loginUser.getId(), travelOnId);
    if (!isAuthor) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "여행On 작성자만 수정·삭제할 수 있습니다.");
    }
  }

  /**
   * 해당 답변(opinion)의 작성자인지 확인
   * @param opinionId 확인할 답변 ID
   * @param loginUser 로그인한 사용자 정보
   * @throws NotFoundException
   * @throws ForbiddenException
   */
  private void isOpinionAuthor(long opinionId, LoginUser loginUser) throws NotFoundException, ForbiddenException {
    boolean isAuthor;

    isAuthor = opinionService.isAuthor(loginUser.getId(), opinionId);
    if (!isAuthor) {
      throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "답변 작성자만 수정·삭제할 수 있습니다.");
    }
  }
}
