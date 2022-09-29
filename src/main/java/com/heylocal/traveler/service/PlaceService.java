package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.PlaceMapper;
import com.heylocal.traveler.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.heylocal.traveler.dto.PlaceDto.PlaceResponse;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;

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
}
