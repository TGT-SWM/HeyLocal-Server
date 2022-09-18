package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.travelon.TransportationType;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ PlanRepository.class })
class PlanRepositoryTest {
	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("플랜 저장")
	void saveTest() {
		// GIVEN - 플랜
		Plan plan = createPlan();

		// WHEN
		Plan savedPlan = planRepository.save(plan);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 저장한 엔티티가 잘 반환되는지
				() -> Assertions.assertThat(savedPlan).isSameAs(plan),
				// 성공 케이스 - 2 - 엔티티의 PK가 잘 generated 되었는지
				() -> Assertions.assertThat(savedPlan.getId()).isNotNull()
		);
	}

	@Test
	@DisplayName("플랜 삭제")
	void removeTest() {
		// GIVEN
		Plan plan = createPlan();
		em.persist(plan);
		long planId = plan.getId();

		// WHEN
		planRepository.remove(plan);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 플랜 삭제 성공
				() -> Assertions.assertThat(em.find(Plan.class, planId)).isNull()
		);
	}

	@Test
	@DisplayName("작성한 플랜 목록 조회")
	void findByUserIdTest() {
		// GIVEN - 여행 On의 지역
		Region region = createRegion();
		em.persist(region);

		// GIVEN - 여행 On 작성자
		User user = createUser();
		em.persist(user);
		long userId = user.getId();

		// plansCount만큼 플랜 저장
		final int plansCount = 10;
		for (int i = 0; i < plansCount; ++i) {
			TravelOn travelOn = createTravelOn(region, user);
			em.persist(travelOn);
			em.persist(createPlan(user, travelOn));
		}

		// WHEN - 성공 케이스
		List<Plan> foundCasePlans = planRepository.findByUserId(userId);

		// WHEN - 실패 케이스
		final long noPlansUserId = userId + 1;
		List<Plan> notFoundCasePlans = planRepository.findByUserId(noPlansUserId);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 작성한 모든 플랜을 잘 조회하는지
				() -> Assertions.assertThat(foundCasePlans.size()).isSameAs(plansCount),
				// 실패 케이스 - 1 - 작성한 플랜이 없는 경우에 빈 결과를 반환하는지
				() -> Assertions.assertThat(notFoundCasePlans).isEmpty()
		);
	}

	@Test
	@DisplayName("ID로 플랜 조회")
	void findByIdTest() {
		// GIVEN
		Plan plan = createPlan();
		em.persist(plan);
		long planId = plan.getId();
		long notFoundPlanId = planId + 1;

		// WHEN
		Optional<Plan> optPlan = planRepository.findById(planId);
		Optional<Plan> notFoundOptPlan = planRepository.findById(notFoundPlanId);

		// THEN
		assertAll(
				// 성공 케이스 - 1 - 플랜 조회 결과가 존재하는지
				() -> Assertions.assertThat(optPlan).isPresent(),
				// 성공 케이스 - 2 - 플랜 조회 결과가 기대와 일치하는지
				() -> Assertions.assertThat(optPlan.get()).isSameAs(plan),
				// 실패 케이스 - 1 - 존재하지 않는 플랜 조회
				() -> Assertions.assertThat(notFoundOptPlan).isEmpty()
		);
	}

	/**
	 * <pre>
	 * 새 Plan 객체를 반환합니다.
	 * @param user Plan을 작성한 사용자
	 * @return Plan 객체
	 * </pre>
	 */
	private Plan createPlan(User user, TravelOn travelOn) {
		return Plan.builder()
				.title("TITLE")
				.travelOn(travelOn)
				.user(user)
				.build();
	}

	/**
	 * <pre>
	 * 임의의 새 Plan 객체를 반환합니다.
	 * @return Plan 객체
	 * </pre>
	 */
	private Plan createPlan() {
		// Region 생성
		Region region = createRegion();
		em.persist(region);

		// User 생성
		User user = createUser();
		em.persist(user);

		// TravelOn 생성
		TravelOn travelOn = createTravelOn(region, user);
		em.persist(travelOn);

		// Plan 반환
		return createPlan(user, travelOn);
	}

	/**
	 * <pre>
	 * 새 Region 객체를 반환합니다.
	 * @return Region 객체
	 * </pre>
	 */
	private Region createRegion() {
		return Region.builder()
				.state("STATE")
				.city("CITY")
				.build();
	}

	/**
	 * <pre>
	 * 새 User 객체를 반환합니다.
	 * @return User 객체
	 * </pre>
	 */
	private User createUser() {
		return User.builder()
				.accountId("ACCOUNT_ID")
				.password("PASSWORD")
				.nickname("NICKNAME")
				.userRole(UserRole.TRAVELER)
				.build();
	}

	/**
	 * <pre>
	 * 새 TravelOn 객체를 반환합니다.
	 * @param region 여행 On의 지역
	 * @param author 여행 On의 작성자
	 * @return TravelOn 객체
	 * </pre>
	 */
	private TravelOn createTravelOn(Region region, User author) {
		return TravelOn.builder()
				.title("TITLE")
				.description("DESCRIPTION")
				.views(1)
				.region(region)
				.author(author)
				.travelStartDate(LocalDate.now())
				.travelEndDate(LocalDate.now())
				.transportationType(TransportationType.PUBLIC)
				.accommodationMaxCost(0)
				.foodMaxCost(0)
				.drinkMaxCost(0)
				.build();
	}
}