package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 프로필 수정을 위한 요청 DTO")
	public static class UserProfileRequest {
		String imageUrl;
		String nickname;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "사용자 프로필 응답 DTO")
	public static class UserProfileResponse {
		String imageUrl;
		String nickname;
		int knowHow;
		long ranking;
	}
}
