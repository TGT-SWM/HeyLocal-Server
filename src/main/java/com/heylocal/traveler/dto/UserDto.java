package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
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

		public UserProfileResponse(UserProfile entity, long ranking) {
			this.imageUrl = entity.getImageUrl();
			this.nickname = entity.getUser().getNickname();
			this.knowHow = entity.getKnowHow();
			this.ranking = ranking;
		}
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
		private String imageUrl;
		private int knowHow;

		public UserResponse(User user) {
			this.id = user.getId();
			this.accountId = user.getAccountId();
			this.nickname = user.getNickname();
			this.imageUrl = user.getUserProfile().getImageUrl();
			this.knowHow = user.getUserProfile().getKnowHow();
		}
	}
}
