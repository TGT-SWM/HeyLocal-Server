/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : RegionService
 * author         : 우태균
 * date           : 2022/09/06
 * description    : 지역 관련 서비스
 */

package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.BadRequestCode;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.mapper.RegionMapper;
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
    result = regionList.stream().map(RegionMapper.INSTANCE::toResponseDto).collect(Collectors.toList());

    return result;
  }

  /**
   * id 로 Region 조회
   * @param regionId
   * @return
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public RegionResponse inquiryRegions(long regionId) throws NotFoundException {
    RegionResponse response;
    Optional<Region> regionOptional = regionRepository.findById(regionId);

    if (regionOptional.isEmpty()) {
      throw new NotFoundException(NotFoundCode.NO_INFO, "해당 지역 id가 존재하지 않습니다.");
    }

    response = RegionMapper.INSTANCE.toResponseDto(regionOptional.get());

    return response;
  }

  /**
   * 모든 Region 조회
   * @return
   */
  @Transactional(readOnly = true)
  public List<RegionResponse> inquiryAllRegions() {
    List<RegionResponse> result;
    List<Region> regionList = regionRepository.findAll();

    result = regionList.stream().map(RegionMapper.INSTANCE::toResponseDto).collect(Collectors.toList());

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

  /**
   * 해당 Region 의 주소인지 확인하는 메서드
   * @param address 확인할 주소
   * @param regionId 비교할 Region의 ID
   * @return
   * @throws BadRequestException
   * @throws NotFoundException
   */
  @Transactional(readOnly = true)
  public RegionDto.RegionAddressCheckResponse checkAddressAsRegion(String address, long regionId) throws BadRequestException, NotFoundException {
    Region regionByAddress = getRegionByAddress(address).orElseThrow(
        () -> new NotFoundException(NotFoundCode.NO_INFO, "주소정보가 잘못되었습니다.")
    );

    boolean isSameRegionAddress = regionByAddress.getId() == regionId;

    return RegionDto.RegionAddressCheckResponse.builder()
        .isSameRegionAddress(isSameRegionAddress)
        .build();
  }
}
