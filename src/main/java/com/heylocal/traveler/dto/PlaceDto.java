package com.heylocal.traveler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class PlaceDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "장소 상세 정보 응답 DTO")
	public static class PlaceResponse {
		long id;
	}
}
