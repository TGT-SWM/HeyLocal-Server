package com.heylocal.traveler.dto;

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
		 * 클라이언트가 마지막으로 받은 아이템의 ID이며,
		 * null인 경우 아이템을 처음부터 조회합니다.
		 */
		Long lastId;

		/** 응답으로 받고자 하는 아이템의 최대 개수 */
		int size;
	}
}
