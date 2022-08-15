package com.heylocal.traveler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PageDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	static public class PageRequest {
		int page;
		int pageSize;
	}
}
