/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : RootApi
 * author         : 우태균
 * date           : 2022/10/13
 * description    : Root 경로 API 인터페이스
 */

package com.heylocal.traveler.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/")
public interface RootApi {
	@Operation(tags = {"Root"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "호출 성공"),
	})
	@GetMapping()
	String root();
}
