package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.post.Post;
import com.heylocal.traveler.domain.profile.TravelerProfile;
import com.heylocal.traveler.domain.user.Manager;
import com.heylocal.traveler.domain.user.Traveler;
import com.heylocal.traveler.domain.userreview.ManagerReview;
import com.heylocal.traveler.dto.PageDto.PageRequest;
import com.heylocal.traveler.repository.ManagerRepository;
import com.heylocal.traveler.repository.ManagerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.ManagerDto.*;

@Service
@RequiredArgsConstructor
public class ManagerService {
	private final ManagerRepository managerRepository;

	private final ManagerReviewRepository managerReviewRepository;

	/**
	 * <pre>
	 * 매니저 프로필의 함축된 정보를 조회
	 * </pre>
	 * @param userId 매니저의 아이디(PK)
	 * @return 매니저 프로필의 함축 버전인 ManagerProfileSimpleResponse 객체
	 */
	@Transactional
	public ManagerProfileSimpleResponse findSimpleProfileById(long userId) {
		// 매니저 조회
		Manager manager = managerRepository.findOne(userId);
		if (manager == null) {
			return null;
		}

		return ManagerProfileSimpleResponse.from(manager);
	}

	/**
	 * <pre>
	 * 매니저 프로필을 조회
	 * </pre>
	 * @param userId 매니저의 아이디(PK)
	 * @return 매니저 프로필 정보를 담은 ManagerProfileResponse 객체
	 */
	@Transactional
	public ManagerProfileResponse findProfileById(long userId) {
		// 매니저 조회
		Manager manager = managerRepository.findOne(userId);
		if (manager == null) {
			return null;
		}

		// 매니저 포스트 조회
		List<Post> postList = manager.getPostList();
		List<Post> shortPostList = postList.subList(0, Math.min(2, postList.size()));

		return ManagerProfileResponse.from(manager, shortPostList);
	}

	/**
	 * <pre>
	 * 매니저 리뷰들을 조회
	 * </pre>
	 * @param managerId 매니저 ID
	 * @param pageRequest PageRequest 객체
	 * @return 매니저 리뷰들이 담긴 List<ManagerReviewResponse> 객체
	 */
	@Transactional
	public List<ManagerReviewResponse> findReviews(long managerId, PageRequest pageRequest) {
		// 매니저 리뷰 조회
		List<ManagerReview> reviews = managerReviewRepository.findAll(managerId, pageRequest);

		// DTO 변환 로직
		return reviews.stream().map(r -> {
			Traveler writer = r.getTravel().getOrderSheet().getWriter();
			TravelerProfile profile = (TravelerProfile) writer.getUserProfile();

			return ManagerReviewResponse.builder()
					.writerNickname(writer.getNickname())
					.writerImageUrl(profile.getImageUrl())
					.kindness(r.getKindness())
					.responsiveness(r.getResponsiveness())
					.noteDetail(r.getNoteDetail())
					.notePrecision(r.getNotePrecision())
					.otherOpinion(r.getOtherOpinion())
					.build();
		}).collect(Collectors.toList());
	}
}
