/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : SearchDto
 * author         : 신우진
 * date           : 2022/08/28
 * description    : 검색 관련 DTO
 */

package com.heylocal.traveler.dto;

import com.heylocal.traveler.dto.TravelOnDto.TravelOnSimpleResponse;
import com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class SearchDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "통합 검색 결과 응답 DTO")
	public static class SearchResultResponse {
		List<TravelOnSimpleResponse> travelOns;
		List<UserProfileResponse> users;
	}
}
