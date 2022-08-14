package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.ManagerGrade;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.profile.ManagerResponseTime;
import com.heylocal.traveler.domain.user.Manager;
import lombok.*;

public class ManagerDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ManagerProfileResponse {
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
		private Region activeRegion1;
		private Region activeRegion2;
		String introduction;

		public static ManagerProfileResponse from(Manager manager, ManagerProfile profile) {
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
					.build();
		}
	}
}
