package com.heylocal.traveler.dto;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PageDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "페이징을 위한 요청 DTO")
	static public class PageRequest {
		/**
		 * 응답으로 받고자 하는 첫 아이템의 index
		 */
		@ApiParam(value = "응답으로 받고자 하는 첫 아이템의 index", required = true)
		int firstIndex;

		/** 응답으로 받고자 하는 아이템의 최대 개수 */
		@ApiParam(value = "응답으로 받고자 하는 아이템의 최대 개수", required = true)
		int size;
	}
}
