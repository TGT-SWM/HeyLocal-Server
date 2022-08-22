package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.profile.ManagerGrade;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import com.heylocal.traveler.domain.profile.ManagerResponseTime;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.user.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({ ManagerProfileRepository.class })
class ManagerProfileRepositoryTest {
	@Autowired
	public ManagerProfileRepository managerProfileRepository;

	@Autowired
	public EntityManager em;

	@Test
	@DisplayName("매니저 프로필 조회")
	void findByUserIdTest() {
		// GIVEN
		Manager manager = createManager();
		ManagerProfile profile = createManagerProfile(manager);

		long id = profile.getUser().getId();
		long notExistsId = id + 1;

		// WHEN
		Optional<ManagerProfile> optResult = managerProfileRepository.findByUserId(id);
		Optional<ManagerProfile> optNotExistsResult = managerProfileRepository.findByUserId(notExistsId);

		// THEN
		// 성공 케이스 - 1 - 프로필 조회 결과가 존재하는 경우
		Assertions.assertTrue(optResult.isPresent());
		// 성공 케이스 - 2 - 프로필 조회 결과가 저장한 것과 일치하는 경우
		// 성공 케이스 1 통과하는 경우에만 실행
		assertThat(optResult.get()).isEqualTo(profile);

		// 실패 케이스 - 1 - 존재하지 않는 매니저의 프로필을 조회하는 경우
		Assertions.assertTrue(optNotExistsResult.isEmpty());
	}

	private Manager createManager() {
		String realName = "김현지";
		String accountId = "accountId";
		String password = "password";
		String phoneNumber = "010-0000-0000";
		UserType userType = UserType.MANAGER;

		Manager manager = Manager.builder()
				.realName(realName)
				.accountId(accountId)
				.password(password)
				.phoneNumber(phoneNumber)
				.userType(userType)
				.build();
		em.persist(manager);
		return manager;
	}

	private ManagerProfile createManagerProfile(Manager manager) {
		ManagerProfile profile = ManagerProfile.builder()
				.user(manager)
				.grade(ManagerGrade.JUNIOR)
				.imageUrl("https://cdna.artstation.com/p/assets/images/images/034/457/380/large/shin-min-jeong-.jpg")
				.totalMatchNum(10)
				.responseTime(ManagerResponseTime.HOUR_1)
				.activateReceiveMatch(true)
				.kindnessAvg(4.5f)
				.responsivenessAvg(4.5f)
				.noteDetailAvg(4.5f)
				.notePrecisionAvg(4.5f)
				.introduction("김현지 매니저입니다.")
				.build();
		em.persist(profile);
		return profile;
	}
}