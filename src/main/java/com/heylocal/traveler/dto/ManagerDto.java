package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.post.Post;
import com.heylocal.traveler.domain.profile.ManagerGrade;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.profile.ManagerResponseTime;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.userreview.ManagerReview;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class ManagerDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	public static class ManagerProfileSimpleResponse {
		Long id;
		String name;
		ManagerGrade grade;
		String imageUrl;
		Float kindnessAvg;
		Float noteDetailAvg;
		Float responsivenessAvg;
		Float notePrecisionAvg;
		Integer totalMatchNum;
		ManagerResponseTime responseTime;
		Region activeRegion1;
		Region activeRegion2;

		public static ManagerProfileSimpleResponse from(Manager manager) {
			return ManagerProfileResponse.from(manager, null);
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	public static class ManagerProfileResponse extends ManagerProfileSimpleResponse {
		String introduction;
		List<Post> postList;
		List<ManagerReview> reviewList;

		public static ManagerProfileResponse from(Manager manager, List<Post> postList) {
			ManagerProfile profile = (ManagerProfile) manager.getUserProfile();

			return ManagerProfileResponse.builder()
					.id(manager.getId())
					.name(manager.getRealName())
					.grade(profile.getGrade())
					.imageUrl(profile.getImageUrl())
					.kindnessAvg(profile.getKindnessAvg())
					.noteDetailAvg(profile.getNoteDetailAvg())
					.responsivenessAvg(profile.getResponsivenessAvg())
					.notePrecisionAvg(profile.getNotePrecisionAvg())
					.totalMatchNum(profile.getTotalMatchNum())
					.responseTime(profile.getResponseTime())
					.activeRegion1(profile.getActiveRegion1())
					.activeRegion2(profile.getActiveRegion2())
					.introduction(profile.getIntroduction())
					.postList(postList)
					.build();
		}
	}
}
