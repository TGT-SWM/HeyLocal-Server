package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.dto.LoginUser;
import com.heylocal.traveler.dto.OpinionDto.OpinionRequest;
import com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import com.heylocal.traveler.dto.TravelOnDto;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnRequest;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnResponse;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 On 목록 조회 성공 시"),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @GetMapping()
    List<TravelOnSimpleResponse> getTravelOns(
            @Parameter(in = ParameterIn.QUERY, description = "여행 On 목록 조회 요청") TravelOnDto.AllTravelOnGetRequest request
        ) throws NotFoundException;

    @Operation(summary = "여행 On 등록", description = "여행 On을 등록합니다.", tags = {"TravelOns"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "여행 On 등록 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PostMapping(consumes = {"application/json"})
    void createTravelOn(
            @Parameter(in = ParameterIn.DEFAULT, description = "여행 On 정보", required = true) @Validated @RequestBody TravelOnRequest request,
            BindingResult bindingResult,
            @ApiIgnore LoginUser loginUser
    ) throws BadRequestException, NotFoundException;

    @Operation(summary = "여행 On 상세 조회", description = "여행 On의 상세 정보를 조회합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 On 상세 조회 성공 시"),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @GetMapping(value = "/{travelOnId}")
    TravelOnResponse getTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId
    ) throws NotFoundException;

    @Operation(summary = "여행 On 수정", description = "여행 On을 수정합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 On 수정 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PutMapping(value = "/{travelOnId}")
    void updateTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId,
            @Parameter(in = ParameterIn.DEFAULT, description = "여행 On 수정할 정보, \n Region Id 필드는 기존 값을 그대로 넘긴다. 값이 변경되더라도 적요되지 않음.", required = true) @RequestBody TravelOnRequest request,
            BindingResult bindingResult,
            @ApiIgnore LoginUser loginUser
    ) throws BadRequestException, NotFoundException, ForbiddenException;

    @Operation(summary = "여행 On 삭제", description = "여행 On을 삭제합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "여행 On 삭제 성공 시"),
        @ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 삭제할 수 없을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @DeleteMapping(value = "/{travelOnId}")
    void deleteTravelOn(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId,
            @ApiIgnore LoginUser loginUser
    ) throws ForbiddenException, NotFoundException;

    /*
     * Opinions API
     * 아래는 여행 On의 답변 관련 컨트롤러입니다.
     */

    @Operation(summary = "답변 조회", description = "여행 On에 달린 답변의 목록을 조회합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "답변 조회 성공 시"),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @GetMapping("/{travelOnId}/opinions")
    List<OpinionResponse> getOpinions(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId
    ) throws NotFoundException;

    @Operation(summary = "답변 등록", description = "여행 On에 답변을 등록합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "해당 여행On 에 답변 등록 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 등록할 수 없을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{travelOnId}/opinions")
    void createOpinions(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId,
            @Parameter(in = ParameterIn.DEFAULT, description = "답변 정보", required = true) @Validated @RequestBody OpinionRequest request,
            BindingResult bindingResult,
            @ApiIgnore LoginUser loginUser
    ) throws BadRequestException, NotFoundException, ForbiddenException;

    @Operation(summary = "답변 수정", description = "답변을 수정합니다.", tags = {"TravelOns"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "답변 수정 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "403", description = "- `NO_PERMISSION`: 수정할 수 없을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "- `NO_INFO`: 존재하지 않는 정보일 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PutMapping("/{travelOnId}/opinions/{opinionId}")
    void updateOpinion(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId,
            @Parameter(in = ParameterIn.PATH, description = "답변 ID", required = true) @PathVariable long opinionId,
            @Parameter(in = ParameterIn.DEFAULT, description = "답변 정보", required = true) @Validated @RequestBody OpinionRequest request,
            BindingResult bindingResult,
            @ApiIgnore LoginUser loginUser
    ) throws BadRequestException, NotFoundException, ForbiddenException;

    @Operation(summary = "답변 삭제", description = "답변을 삭제합니다.", tags = {"TravelOns"})
    @DeleteMapping("/{travelOnId}/opinions/{opinionId}")
    ResponseEntity<Void> deleteOpinion(
            @Parameter(in = ParameterIn.PATH, description = "여행 On ID", required = true) @PathVariable long travelOnId,
            @Parameter(in = ParameterIn.PATH, description = "답변 ID", required = true) @PathVariable long opinionId
    );
}

