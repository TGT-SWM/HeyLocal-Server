package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.TravelOnsApi;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto;
import com.heylocal.traveler.dto.PageDto;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.service.TravelOnService;
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

  private final TravelOnService travelOnService;

  @Override
  public List<TravelOnSimpleResponse> getTravelOns(long regionId, Boolean withOpinions, TravelOnSortType sortBy, PageDto.PageRequest pageRequest) {
    return null;
  }

  @Override
  public void createTravelOn(TravelOnRequest request,
                             BindingResult bindingResult,
                             LoginUser loginUser) throws BadRequestException {
    if (bindingResult.hasFieldErrors()) {
      String errMsg = bindingResult.getFieldError().getDefaultMessage();
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, errMsg);
    }

    travelOnService.addNewTravelOn(request, loginUser);
  }

  @Override
  public TravelOnResponse getTravelOn(long travelOnId) {
    return null;
  }

  @Override
  public ResponseEntity<Void> updateTravelOn(long travelOnId, TravelOnRequest request) {
    return null;
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
