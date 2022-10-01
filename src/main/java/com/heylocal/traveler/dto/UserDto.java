package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static com.heylocal.traveler.dto.RegionDto.RegionRequest;
import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

public class UserDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 프로필 수정을 위한 요청 DTO")
	public static class UserProfileRequest {
		private String imageUrl;
		private String nickname;
		private String introduce;
		private RegionRequest activityRegion;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 프로필 응답 DTO")
	public static class UserProfileResponse {
		private String introduce;
		private String imageUrl;
		private String nickname;
		private int knowHow;
		private long ranking;
		private RegionResponse activityRegion;
		private int acceptedOpinionCount;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 정보 응답 DTO")
	public static class UserResponse {
		private long id;
		private String accountId;
		private String nickname;
	}
}
