/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.Sample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-12T04:12:44.357Z[GMT]")
@RequestMapping("/ordersheets")
public interface OrderSheetsApi {

    @Operation(summary = "여행 의뢰서 리스트 조회", description = "", tags={ "ordersheet" })
    @GetMapping()
    ResponseEntity<Void> orderSheetsGet(
        @Parameter(in = ParameterIn.QUERY, description = "현재 의뢰서: now, 지난 의뢰서: past", required = true) @RequestParam String status,
        @Parameter(in = ParameterIn.QUERY, description = "조회할 페이지 번호", required = true) @RequestParam int page);


    @Operation(summary = "여행 의뢰서 상세 조회", description = "", tags={ "ordersheet" })
    @GetMapping(value = "/{orderSheetId}")
    ResponseEntity<Void> ordersheetsOrderSheetIdGet(
        @Parameter(in = ParameterIn.PATH, description = "조회할 여행 의뢰서 id", required = true) @PathVariable long orderSheetId);


    @Operation(summary = "여행 의뢰서 등록", description = "의뢰비를 응답받는다.", tags={ "ordersheet" })
    @PostMapping(consumes = { "application/json" })
    ResponseEntity<Void> ordersheetsPost(
        @Parameter(in = ParameterIn.DEFAULT, description = "등록할 여행 의뢰서 정보", required = true) @Validated @RequestBody Sample body);

}

