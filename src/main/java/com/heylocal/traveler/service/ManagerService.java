package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.repository.ManagerProfileRepository;
import com.heylocal.traveler.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.ManagerDto.*;

@Service
@RequiredArgsConstructor
public class ManagerService {
	private final ManagerRepository managerRepository;
	private final ManagerProfileRepository managerProfileRepository;

	@Transactional
	public ManagerProfileResponse findByUserId(Long userId) {
		Manager manager = managerRepository.findOne(userId);
		ManagerProfile profile = managerProfileRepository.findByUserId(userId);

		if (manager == null || profile == null) {
			return null;
		}

		return ManagerProfileResponse.from(manager, profile);
	}
}
