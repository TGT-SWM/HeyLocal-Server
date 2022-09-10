package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

@Service
@RequiredArgsConstructor
public class RegionService {
  private final RegionRepository regionRepository;

  /**
   * state 로 Region 조회
   * @param state
   * @return
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public List<RegionResponse> inquiryRegions(String state) throws NotFoundException {
    List<RegionResponse> result;
    List<Region> regionList = regionRepository.findByState(state);

    if (regionList.size() == 0) {
      throw new NotFoundException(NotFoundCode.NO_INFO, "해당 state가 존재하지 않습니다.");
    }
    result = regionList.stream().map(RegionResponse::new).collect(Collectors.toList());


    return result;
  }

  /**
   * 주소와 Region 엔티티를 매핑해서 Region 엔티티를 반환하는 메서드
   * @param address 매핑할 주소
   * @return 매핑된 Region 엔티티
   * @throws BadRequestException
   */
  @Transactional(readOnly = true)
  public Optional<Region> getRegionByAddress(String address) throws BadRequestException {
    String keyword;
    String[] addressAry;
    String state;
    String city;

    addressAry = address.split(" ");
    if (addressAry.length < 2) {
      throw new BadRequestException(BadRequestCode.BAD_INPUT_FORM, "장소의 주소 형식이 잘못되었습니다.");
    }
    state = addressAry[0];
    city = addressAry[1];

    if (state.contains("제주")) { //제주인 경우
      return regionRepository.findByStateKeyword("제주");

    } else if (city.endsWith("시")) { //city 가 "시"인 경우
      keyword = city.replace("시", "");
      return regionRepository.findByCityKeyword(keyword);

    } else if (city.endsWith("군")) { //city 가 "군"인 경우
      keyword = city.replace("군", "");
      return regionRepository.findByCityKeyword(keyword);

    } else { //특별시나 광역시인 경우
      keyword = state.replace("시", "");
      return regionRepository.findByStateKeyword(keyword);
    }

  }
}
