package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.dto.ManagerDto.ManagerProfileSimpleResponse;
import com.heylocal.traveler.repository.ManagerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.*;

class ManagerServiceTest {
	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private ManagerService managerService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("매니저 간단 프로필 조회")
	void findSimpleProfileById() {
		// GIVEN
		Long managerId = 1L;
		Manager manager = createManager(managerId);
		given(managerRepository.findOne(managerId)).willReturn(manager);

		// WHEN
		ManagerProfileSimpleResponse response = managerService.findSimpleProfileById(managerId);

		// THEN
		// 1. 유효한 결과 반환 (해당 ID의 매니저가 존재)
		Assertions.assertThat(response).isNotNull();
		// 2. 의도한 결과 반환 (매니저 ID가 일치)
		Assertions.assertThat(response.getId()).isEqualTo(managerId);
	}

	@Test
	@DisplayName("매니저 프로필 조회")
	void findProfileById() {
		// GIVEN
		Long managerId = 1L;
		Manager manager = createManager(managerId);
		given(managerRepository.findOne(managerId)).willReturn(manager);

		// WHEN
		ManagerProfileSimpleResponse response = managerService.findSimpleProfileById(managerId);

		// THEN
		// 1. 유효한 결과 반환 (해당 ID의 매니저가 존재)
		Assertions.assertThat(response).isNotNull();
		// 2. 의도한 결과 반환 (매니저 ID가 일치)
		Assertions.assertThat(response.getId()).isEqualTo(managerId);
	}

	// 매니저 객체 생성해 반환
	private Manager createManager(Long id) {
		return Manager.builder()
				.id(id)
				.realName("김현지")
				.userProfile(new ManagerProfile())
				.build();
	}
}