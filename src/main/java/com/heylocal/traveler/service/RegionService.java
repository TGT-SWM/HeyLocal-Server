package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.exception.code.NotFoundCode;
import com.heylocal.traveler.exception.service.BadArgumentException;
import com.heylocal.traveler.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.RegionDto.RegionResponse;

@Service
@RequiredArgsConstructor
public class RegionService {
  private final RegionRepository regionRepository;

  public List<RegionResponse> inquiryRegions(String state) throws BadArgumentException {
    List<RegionResponse> result;
    List<Region> regionList = regionRepository.findByState(state);

    if (regionList.size() == 0) {
      throw new BadArgumentException(NotFoundCode.NO_INFO, "해당 state가 존재하지 않습니다.");
    }
    result = regionList.stream().map(RegionResponse::new).collect(Collectors.toList());


    return result;
  }
}
