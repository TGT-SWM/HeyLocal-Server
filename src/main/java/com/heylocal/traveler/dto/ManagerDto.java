package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.profile.ManagerGrade;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.profile.ManagerResponseTime;
import com.heylocal.traveler.domain.user.Manager;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

		public static ManagerProfileSimpleResponse from(Manager manager, ManagerProfile profile) {
			return ManagerProfileResponse.from(manager, profile);
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	public static class ManagerProfileResponse extends ManagerProfileSimpleResponse {
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
					.build();
		}
	}
}
