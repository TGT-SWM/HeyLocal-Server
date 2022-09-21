package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.dto.PlanDto.*;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.ForbiddenException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.ForbiddenCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.PlaceItemMapper;
import com.heylocal.traveler.mapper.PlanMapper;
import com.heylocal.traveler.repository.PlaceItemRepository;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.repository.PlanRepository;
import com.heylocal.traveler.repository.TravelOnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
	private final PlanRepository planRepository;

	private final TravelOnRepository travelOnRepository;

	private final PlaceRepository placeRepository;

	private final PlaceItemRepository placeItemRepository;

	private final RegionService regionService;

	private final Clock clock;

	/**
	 * <pre>
	 * 작성한 스케줄 정보를 반환합니다.
	 * @param userId 사용자의 아이디
	 * @return 작성한 스케줄 정보
	 * </pre>
	 */
	@Transactional(readOnly = true)
	public PlanListResponse getPlans(long userId) {
		// 작성한 플랜 조회
		List<Plan> plans = planRepository.findByUserId(userId);

		// Plan -> PlanResponse 변환
		List<PlanResponse> planResponses = plans.stream()
				.map(PlanMapper.INSTANCE::toPlanResponseDto)
				.collect(Collectors.toList());

		// 플랜을 날짜에 따라 분류
		LocalDate today = LocalDate.now(clock);
		List<PlanResponse> past = new ArrayList<>();
		List<PlanResponse> ongoing = new ArrayList<>();
		List<PlanResponse> upcoming = new ArrayList<>();

		planResponses.forEach(planResponse -> {
			LocalDate startDate = planResponse.getStartDate();
			LocalDate endDate = planResponse.getEndDate();

			if (today.isBefore(startDate)) {
				upcoming.add(planResponse);
			} else if (today.isAfter(endDate)) {
				past.add(planResponse);
			} else {
				ongoing.add(planResponse);
			}
		});

		// PlanListResponse 생성 및 반환
		return PlanListResponse.builder()
				.past(past)
				.ongoing(ongoing)
				.upcoming(upcoming)
				.build();
	}

	/**
	 * <pre>
	 * 플랜을 생성합니다.
	 * @param userId 로그인 사용자 ID
	 * @param travelOnId 여행 On ID
	 * </pre>
	 */
	@Transactional
	public void createPlan(long userId, long travelOnId) throws NotFoundException, ForbiddenException, BadRequestException {
		// TravelOn 조회 (없으면 예외 발생)
		Optional<TravelOn> optTravelOn = travelOnRepository.findById(travelOnId);
		if (optTravelOn.isEmpty())
			throw new NotFoundException(NotFoundCode.NO_INFO, "여행 On이 존재하지 않습니다.");
		TravelOn travelOn = optTravelOn.get();

		// 권한 확인
		if (travelOn.getAuthor().getId() != userId)
			throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "등록 권한이 없습니다.");

		// 이미 플랜이 존재하는 경우 예외 발생
		if (travelOn.getPlan() != null)
			throw new BadRequestException(BadRequestCode.ALREADY_EXISTS, "이미 플랜이 존재합니다.");

		// Plan 타이틀 가져오기
		String regionName = travelOn.getRegion().getRegionName();
		String planTitle = regionName + " 여행";

		// DaySchedule 생성
		List<DaySchedule> daySchedules = new ArrayList<>();
		LocalDate startDate = travelOn.getTravelStartDate();
		LocalDate endDate = travelOn.getTravelEndDate();
		LocalDate date = startDate;

		while (!date.isAfter(endDate)) {
			DaySchedule daySchedule = DaySchedule.builder()
					.dateTime(date)
					.build();

			daySchedules.add(daySchedule);
			date = date.plusDays(1);
		}

		// Plan 생성하여 저장
		Plan plan = Plan.builder()
				.title(planTitle)
				.travelOn(travelOn)
				.user(travelOn.getAuthor())
				.build();
		daySchedules.forEach(plan::addDaySchedule); // DaySchedule 추가
		planRepository.save(plan); // 저장
	}

	/**
	 * <pre>
	 * 플랜을 수정합니다.
	 * @param planId 플랜 ID
	 * @param request 수정하는 플랜 정보
	 * </pre>
	 */
	@Transactional
	public void updatePlan(long planId, long userId, PlanUpdateRequest request) throws ForbiddenException, NotFoundException {
		// 플랜 조회 (없으면 예외 발생)
		Optional<Plan> optPlan = planRepository.findById(planId);
		if (optPlan.isEmpty())
			throw new NotFoundException(NotFoundCode.NO_INFO, "플랜이 존재하지 않습니다.");
		Plan plan = optPlan.get();

		// 수정 권한 검증
		if (plan.getUser().getId() != userId)
			throw new ForbiddenException(ForbiddenCode.NO_PERMISSION, "수정 권한이 없습니다.");

		// 플랜 정보 수정
		String title = request.getTitle();
		plan.updateTitle(title);
	}

	/**
	 * <pre>
	 * 해당 플랜에 포함된 장소 리스트를 일자별로 나누어 반환
	 * @param planId 플랜 ID
	 * @return
	 * @throws NotFoundException
	 * </pre>
	 */
	@Transactional(readOnly = true)
	public List<PlanPlacesResponse> getPlacesInPlan(long planId) throws NotFoundException {
		// Plan 조회
		// Plan이 존재하지 않는 경우에는 예외 발생
		Optional<Plan> optPlan = planRepository.findById(planId);
		if (optPlan.isEmpty())
			throw new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 플랜입니다.");
		Plan plan = optPlan.get();

		// DTO 변환
		// List<DaySchedule> -> List<PlanPlacesReponse>
		List<DaySchedule> daySchedules = plan.getDayScheduleList();
		return daySchedules.stream()
				.map(PlanMapper.INSTANCE::toPlanPlacesResponseDto)
				.collect(Collectors.toList());
	}

	/**
	 * <pre>
	 * 해당 플랜의 장소 목록을 업데이트합니다.
	 * @param planId 플랜 ID
	 * @param request 장소 정보
	 * </pre>
	 */
	@Transactional
	public void updatePlacesInPlan(long planId, PlanSchedulesRequest request) throws NotFoundException {
		// Plan 조회
		// Plan이 존재하지 않는 경우에는 예외 발생
		Optional<Plan> optPlan = planRepository.findById(planId);
		if (optPlan.isEmpty())
			throw new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 플랜입니다.");
		Plan plan = optPlan.get();

		// DTO 변환
		List<ScheduleRequest> scheduleRequests = request.getSchedules();
		List<List<PlaceItem>> schedules = scheduleRequests.stream()
				.map(scheduleRequest -> { // List<ScheduleRequest> -> List<List<PlaceItem>>
					return scheduleRequest.getPlaces().stream()
							.map(PlaceItemMapper.INSTANCE::toPlaceItemEntity) // ScheduleRequest -> List<PlaceItem>
							.collect(Collectors.toList());
				})
				.collect(Collectors.toList());

		// 장소 엔티티 저장
		for (List<PlaceItem> placeItems: schedules) {
			for (PlaceItem placeItem: placeItems) {
				// 레포지터리에서 Place 검색
				Place place = placeItem.getPlace();
				Optional<Place> optPlace = placeRepository.findById(place.getId());

				// Place가 있는 경우 가져오고, 없으면 저장하기
				optPlace.ifPresentOrElse(placeItem::setPlace, () -> {
					try {
						regionService.getRegionByAddress(place.getAddress()).ifPresent(region -> {
							place.updateRegion(region);
							placeRepository.save(place);
						});
					} catch (BadRequestException e) {}
				});
			}
		}

		// 장소 스케줄 업데이트
		int i = 0;
		for (DaySchedule daySchedule: plan.getDayScheduleList()) {
			List<PlaceItem> placeItems = schedules.get(i++);

			// 스케줄 비우기
			for (PlaceItem placeItem: daySchedule.getPlaceItemList())
				placeItemRepository.remove(placeItem);
			daySchedule.getPlaceItemList().clear();

			// 장소 추가
			for (PlaceItem placeItem: placeItems)
				daySchedule.addPlaceItem(placeItem);
		}
	}
}
