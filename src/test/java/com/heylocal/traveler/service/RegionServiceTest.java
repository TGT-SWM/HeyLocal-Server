package com.heylocal.traveler.service;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.dto.RegionDto;
import com.heylocal.traveler.exception.BadRequestException;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;

class RegionServiceTest {
  @Mock
  private RegionRepository regionRepository;
  private RegionService regionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    regionService = new RegionService(regionRepository);
  }

  @Test
  @DisplayName("state 으로 Region 조회")
  void inquiryRegionsTest() throws NotFoundException {
    //GIVEN
    String existState = "existState";
    String notExistState = "notExistState";

    //Mock 행동 정의 - RegionRepository
    Region region1 = Region.builder()
            .id(1L)
            .state(existState)
            .city("existCity1")
            .build();
    Region region2 = Region.builder()
        .id(2L)
        .state(existState)
        .city("existCity2")
        .build();
    List<Region> regionList = new ArrayList<>();
    regionList.add(region1);
    regionList.add(region2);
    willReturn(regionList).given(regionRepository).findByState(eq(existState));
    willReturn(new ArrayList<Region>()).given(regionRepository).findByState(eq(notExistState));

    //WHEN
    List<RegionDto.RegionResponse> succeedResult = regionService.inquiryRegions(existState);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 state 조회시
        () -> assertDoesNotThrow(() -> regionService.inquiryRegions(existState)),
        () -> assertEquals(2, succeedResult.size()),
        //실패 케이스 - 1 - 존재하지 않는 state 조회시
        () -> assertThrows(NotFoundException.class, () -> regionService.inquiryRegions(notExistState))
    );
  }

  //TODO - inquiryRegions(long regionId)

  @Test
  @DisplayName("주소 -> Region 매핑 - 성공 케이스")
  void getRegionByAddressSucceedTest() throws BadRequestException {
    //GIVEN
    String metropolitanKeyword = "부산";
    String generalCityKeyword = "안산";
    String smallCityKeyword = "담양";
    String jejuKeyword = "제주";
    String metropolitanAddress = metropolitanKeyword + " 해운대구 중동2로10번길 32-10";
    String generalCityAddress = "경기도 " + generalCityKeyword + "시 단원구 중앙대로 443";
    String smallCityAddress = "전라남도 " + smallCityKeyword + "군 봉산면 기곡리 293-1";
    String jejuAddress = jejuKeyword + "도 제주시 조천읍 함덕리 528";

    //WHEN
    regionService.getRegionByAddress(metropolitanAddress);
    regionService.getRegionByAddress(generalCityAddress);
    regionService.getRegionByAddress(smallCityAddress);
    regionService.getRegionByAddress(jejuAddress);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 광역시·특별시
        () -> then(regionRepository).should(times(1)).findByStateKeyword(eq(metropolitanKeyword)),
        //성공 케이스 - 2 - 일반 시(city)
        () -> then(regionRepository).should(times(1)).findByCityKeyword(eq(generalCityKeyword)),
        //성공 케이스 - 3 - 군
        () -> then(regionRepository).should(times(1)).findByCityKeyword(eq(smallCityKeyword)),
        //성공 케이스 - 4 - 제주
        () -> then(regionRepository).should(times(1)).findByStateKeyword(eq(jejuKeyword))
    );
  }

  @Test
  @DisplayName("주소 -> Region 매핑 - 잘못된 주소 형식")
  void getRegionByAddressWrongAddressFormatTest() {
    //GIVEN
    String wrongAddress = "부산해운대구중동2로10번길32-10";

    //WHEN

    //THEN
    assertThrows(BadRequestException.class, () -> regionService.getRegionByAddress(wrongAddress));
  }
}