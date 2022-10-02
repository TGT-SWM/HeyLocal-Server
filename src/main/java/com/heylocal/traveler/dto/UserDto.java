package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
		@NotEmpty
		private String nickname;
		@Length(max = 255)
		private String introduce;
		@PositiveOrZero
		private int activityRegionId;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 프로필 응답 DTO")
	public static class UserProfileResponse {
		private long userId;
		private String introduce;
		private String nickname;
		private String profileImgDownloadUrl;
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
