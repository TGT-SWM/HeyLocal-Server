package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.TravelOnsApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.ForbiddenException;
import com.heylocal.traveler.exception.controller.NotFoundException;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.service.TravelOnService;
import com.heylocal.traveler.util.error.BindingErrorMessageProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.heylocal.traveler.dto.TravelOnDto.*;

@Slf4j
@Tag(name = "TravelOns")
@RestController
@RequiredArgsConstructor
public class TravelOnsController implements TravelOnsApi {
  private final BindingErrorMessageProvider errorMessageProvider;

  private final TravelOnService travelOnService;

  /**
   * 여행On 목록 조회 핸들러
   * @param request 조회 조건
   * @return
   * @throws NotFoundException Request 한 Region 이 존재하지 않는 경우
   */
  @Override
  public List<TravelOnSimpleResponse> getTravelOns(AllTravelOnGetRequest request) throws NotFoundException {
    List<TravelOnSimpleResponse> response;

    try {
      response = travelOnService.inquirySimpleTravelOns(request);
    } catch (BadArgumentException e) {
      throw new NotFoundException(e.getCode(), e.getDescription());
    }

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

    try {
      travelOnService.addNewTravelOn(request, loginUser);
    } catch (BadArgumentException e) {
      throw new NotFoundException(e.getCode(), e.getDescription());
    }
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

    try {
      response = travelOnService.inquiryTravelOn(travelOnId);
    } catch (BadArgumentException e) {
      throw new NotFoundException(e.getCode(), e.getDescription());
    }

    return response;
  }

  /**
   * 여행On 수정 핸들러
   * @param travelOnId 수정할 여행On ID
   * @param request 수정 정보
   * @param bindingResult
   * @param loginUser
   * @throws BadRequestException
   * @throws NotFoundException
   */
  @Override
  public void updateTravelOn(long travelOnId,
                             TravelOnRequest request,
                             BindingResult bindingResult,
                             LoginUser loginUser) throws BadRequestException, NotFoundException, ForbiddenException {
    boolean isAuthor = false;

    if (bindingResult.hasFieldErrors()) {
      String fieldErrMsg = errorMessageProvider.getFieldErrMsg(bindingResult);
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, fieldErrMsg);
    }

    try {
      //수정 권한 확인
      isAuthor = travelOnService.isAuthor(loginUser.getId(), travelOnId);
      if (!isAuthor) {
        throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "수정 권한이 없습니다.");
      }

      //수정
      travelOnService.updateTravelOn(request, travelOnId);
    } catch (BadArgumentException e) {
      throw new NotFoundException(e.getCode(), e.getDescription());
    }
  }

  @Override
  public ResponseEntity<Void> deleteTravelOn(long travelOnId) {
    return null;
  }

  @Override
  public List<OpinionDto.OpinionResponse> getOpinions(long travelOnId) {
    return null;
  }

  @Override
  public ResponseEntity<Void> createOpinions(long travelOnId, OpinionDto.OpinionRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<Void> updateOpinion(long travelOnId, long opinionId, OpinionDto.OpinionRequest request) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteOpinion(long travelOnId, long opinionId) {
    return null;
  }
}
