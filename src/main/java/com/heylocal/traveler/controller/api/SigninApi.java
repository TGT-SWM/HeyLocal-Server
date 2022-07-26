/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : SigninApi
 * author         : 우태균
 * date           : 2022/08/12
 * description    : 로그인 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.heylocal.traveler.dto.SigninDto.SigninRequest;
import static com.heylocal.traveler.dto.SigninDto.SigninResponse;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-12T04:12:44.357Z[GMT]")
@RequestMapping("/signin")
public interface SigninApi {

    @Operation(summary = "로그인", description = "", tags = {"Signin"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "- `BAD_INPUT_FORM`: 입력 값의 형식이 올바르지 않을 때", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class))),
        @ApiResponse(responseCode = "401", description = "- `NOT_EXIST_SIGNIN_ACCOUNT_ID`: 아이디 오류\n\n- `WRONG_SIGNIN_PASSWORD`: 비밀번호 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PostMapping(consumes = { "application/json" })
    SigninResponse signin(
        @Parameter(in = ParameterIn.DEFAULT, description = "", required=true) @Validated @RequestBody SigninRequest request,
        BindingResult bindingResult) throws BadRequestException, UnauthorizedException;

}

