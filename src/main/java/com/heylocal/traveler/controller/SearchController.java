package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.SearchApi;
import com.heylocal.traveler.dto.SearchDto;
import com.heylocal.traveler.dto.TravelOnDto;
import com.heylocal.traveler.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search")
@RestController
public class SearchController implements SearchApi {
	/**
	 * @param query
	 * @return
	 */
	@Override
	public SearchDto.SearchResultResponse getSearchResults(String query) {
		return null;
	}

	/**
	 * @param query
	 * @return
	 */
	@Override
	public List<TravelOnDto.TravelOnSimpleResponse> getTravelOnSearchResults(String query) {
		return null;
	}

	/**
	 * @param query
	 * @return
	 */
	@Override
	public List<UserDto.UserProfileResponse> getUserSearchResults(String query) {
		return null;
	}
}
