package com.heylocal.traveler.dto;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Positive;

public class PageDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Schema(description = "페이징을 위한 요청 DTO")
	static public class PageRequest {
		/**
		 * 클라이언트가 받은 마지막 아이템의 id(pk)
		 * null 이면 처음부터
		 */
		@ApiParam(value = "클라이언트가 받은 마지막 아이템의 id(pk), null 이면 처음부터")
		Long lastItemId;

		/** 응답으로 받고자 하는 아이템의 최대 개수 */
		@ApiParam(value = "응답으로 받고자 하는 아이템의 최대 개수", required = true)
		@Positive
		int size;
	}
}
