/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : PlaceService
 * author         : 우태균
 * date           : 2022/09/29
 * description    : 장소 관련 서비스
 */

package com.heylocal.traveler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.PlaceMapper;
import com.heylocal.traveler.repository.PlaceRepository;
import com.heylocal.traveler.util.http.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;
import static com.heylocal.traveler.dto.PlaceDto.PlaceWithOpinionSizeResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
  @Value("${crawling.server.url.menu}")
  private String crawlingMenuUrl;
  private final PlaceRepository placeRepository;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  /**
   * 장소 ID 로 장소를 조회하는 메서드
   * @param placeId 조회할 장소의 ID
   * @return
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public PlaceResponse inquiryPlace(long placeId) throws NotFoundException {
    Place place = placeRepository.findById(placeId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 장소 ID입니다.")
    );
    PlaceResponse response = PlaceMapper.INSTANCE.toPlaceResponseDto(place);

    return response;
  }

  /**
   * 가장 많은 답변으로 선택된 장소를 조회하는 메서드
   * @param size 조회할 장소 개수 (null 인 경우, 5개)
   * @return
   */
  @Transactional(readOnly = true)
  public List<PlaceWithOpinionSizeResponse> inquiryMostOpinedPlace(@Nullable Integer size) {
    List<Place> findResult;
    List<PlaceWithOpinionSizeResponse> result;

    //size 가 null 이라면 5로 설정
    size = (size == null) ? 5 : size;

    //가장 많은 답변으로 선택된 장소 조회
    findResult = placeRepository.findPlaceOrderByOpinionSizeDesc(size);

    //List<Place> -> List<PlaceResponse>
    result = findResult.stream().map(PlaceMapper.INSTANCE::toPlaceWithOpinionSizeResponseDto).collect(Collectors.toList());

    return result;
  }

  /**
   * 장소(음식점·카페)의 기타 정보(영업시간·메뉴)를 조회하는 메서드
   * @param placeId 조회할 장소 ID
   * @return
   */
  public String inquirySubInfo(long placeId) throws NotFoundException {
    String crawlingResult;
    validatePlaceId(placeId); //존재하는 장소 ID 인지 검사

    //크롤링 서버에서 응답 받기
    try {
      crawlingResult = httpClient.get(crawlingMenuUrl + "?placeId=" + placeId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return crawlingResult;
  }

  private void validatePlaceId(long placeId) throws NotFoundException {
    placeRepository.findById(placeId).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "존재하지 않는 장소 ID 입니다.")
    );
  }

}
