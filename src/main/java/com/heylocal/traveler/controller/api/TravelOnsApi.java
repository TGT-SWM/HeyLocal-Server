package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto.OpinionRequest;
import com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnResponse;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSortType;
import com.heylocal.traveler.exception.controller.BadRequestException;
import com.heylocal.traveler.exception.controller.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequestMapping("/travel-ons")
public interface TravelOnsApi {
    @Operation(summary = "전체 여행 On 조회", description = "전체 여행 On의 목록을 조회합니다.", tags = {"TravelOns"})
    @GetMapping()
    List<TravelOnSimpleResponse> getTravelOns(
            @Parameter(in = ParameterIn.QUERY, description = "지역 ID", required = false) long regionId,
            @Parameter(in = ParameterIn.QUERY, description = "답변 있는 것(true), 없는 것(false), 전체(null)", required = false) Boolean withOpinions,
            @Parameter(in = ParameterIn.QUERY, description = "정렬 기준(DATE, VIEWS, OPINIONS)", required = true) TravelOnSortType sortBy,
            @Parameter(in = ParameterIn.QUERY, description = "페이징", required = true) PageRequest pageRequest
    );

    @Operation(summary = "여행 On 등록", description = "여행 On을 등록합니다.", tags = {"TravelOns"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "여행 On 등록 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
    })
    @PostMapping(consumes = {"application/json"})
    void createTravelOn(
            @Parameter(in = ParameterIn.DEFAULT, description = "여행 On 정보", required = true) @Validated @RequestBody TravelOnRequest request,
            BindingResult bindingResult,
            @ApiIgnore LoginUser loginUser
    ) throws BadRequestException, NotFoundException;

    @Operation(summary = "여행 On 상세 조회정", description = "여행 On의 상세 정보를 조회합니다.", tags = {"TravelOns"})
    @GetMapping(value = "/{travelOnId}")
    TravelOnResponse getTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId
    );

    @Operation(summary = "여행 On 수정", description = "여행 On을 수정합니다.", tags = {"TravelOns"})
    @PutMapping(value = "/{travelOnId}")
    ResponseEntity<Void> updateTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId,
            @Parameter(in = ParameterIn.DEFAULT, description = "여행 On 정보", required = true) @RequestBody TravelOnRequest request
    );

    @Operation(summary = "여행 On 삭제", description = "여행 On을 삭제합니다.", tags = {"TravelOns"})
    @DeleteMapping(value = "/{travelOnId}")
    ResponseEntity<Void> deleteTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId
    );

    /*
     * Opinions API
     * 아래는 여행 On의 답변 관련 컨트롤러입니다.
     */

    @Operation(summary = "답변 조회", description = "여행 On에 달린 답변의 목록을 조회합니다.", tags = {"TravelOns"})
    @GetMapping("/{travelOnId}/opinions")
    List<OpinionResponse> getOpinions(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId
    );

    @Operation(summary = "답변 등록", description = "여행 On에 답변을 등록합니다.", tags = {"TravelOns"})
    @PostMapping("/{travelOnId}/opinions")
    ResponseEntity<Void> createOpinions(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId,
            @Parameter(in = ParameterIn.DEFAULT, description = "답변 정보", required = true) @RequestBody OpinionRequest request
    );

    @Operation(summary = "답변 수정", description = "답변을 수정합니다.", tags = {"TravelOns"})
    @PutMapping("/{travelOnId}/opinions/{opinionId}")
    ResponseEntity<Void> updateOpinion(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId,
            @Parameter(in = ParameterIn.PATH, description = "답변 ID", required = true) long opinionId,
            @Parameter(in = ParameterIn.DEFAULT, description = "답변 정보", required = true) @RequestBody OpinionRequest request
    );

    @Operation(summary = "답변 삭제", description = "답변을 삭제합니다.", tags = {"TravelOns"})
    @DeleteMapping("/{travelOnId}/opinions/{opinionId}")
    ResponseEntity<Void> deleteOpinion(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) long travelOnId,
            @Parameter(in = ParameterIn.PATH, description = "답변 ID", required = true) long opinionId
    );
}

