package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.SearchDto.SearchResultResponse;
import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/search")
public interface SearchApi {
    @Operation(summary = "통합 검색", description = "통합 검색 결과를 조회합니다.", tags = {"Search"})
    @GetMapping()
    SearchResultResponse getSearchResults(
            @Parameter(in = ParameterIn.QUERY, description = "검색어", required = true) String query
    );

    @Operation(summary = "여행 On 검색", description = "여행 On 검색 결과를 조회합니다.", tags = {"Search"})
    @GetMapping("/travel-ons")
    List<TravelOnSimpleResponse> getTravelOnSearchResults(
            @Parameter(in = ParameterIn.QUERY, description = "검색어", required = true) String query
    );

    @Operation(summary = "사용자 검색", description = "사용자 검색 결과를 조회합니다.", tags = {"Search"})
    @GetMapping("/users")
    List<UserProfileResponse> getUserSearchResults(
            @Parameter(in = ParameterIn.QUERY, description = "검색어", required = true) String query
    );
}

