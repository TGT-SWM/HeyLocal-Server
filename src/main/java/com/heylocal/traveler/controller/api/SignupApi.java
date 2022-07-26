/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : SignupApi
 * author         : 우태균
 * date           : 2022/08/12
 * description    : 회원가입 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.heylocal.traveler.dto.SignupDto.SignupRequest;
import static com.heylocal.traveler.dto.SignupDto.UserInfoCheckResponse;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-12T04:12:44.357Z[GMT]")
@RequestMapping("/signup")
public interface SignupApi {

    @Operation(summary = "아이디 중복 확인", description = "", tags = {"Signup"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전체 지역 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "- `SHORT_OR_LONG_ACCOUNT_ID_LENGTH`: 계정 아이디가 너무 짧거나 길 경우\n\n- `WRONG_ACCOUNT_ID_FORMAT`: 계정 아이디 문자 조합이 틀린 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @GetMapping("/accountid")
    UserInfoCheckResponse checkSignupId(
        @Parameter(in = ParameterIn.QUERY, description = "확인할 아이디", required = true) @RequestParam String accountId) throws BadRequestException;

    @Operation(summary = "회원가입", description = "", tags = {"Signup"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공 시"),
        @ApiResponse(responseCode = "400", description = "- `WRONG_PASSWORD_FORMAT`: 비밀번호 형식이 틀린 경우\n\n- `WRONG_NICKNAME_FORMAT`: 닉네임 형식이 틀린 경우\n\n- `ALREADY_EXIST_USER_INFO`: 이미 존재하는 사용자 정보인 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { "application/json" })
    void signup(
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true) @Validated @RequestBody SignupRequest request,
        BindingResult bindingResult) throws BadRequestException;

}

