package com.heylocal.traveler.domain.place;

import com.heylocal.traveler.domain.Region;

/**
 * 장소 (방문지)
 */
public class Place {
  private Long id;
  private PlaceCategory category;
  private String name;
  private String roadAddress; //도로명주소
  private String address; //구주소
  private Region region;
  private String imageUrl;
  private String link; //네이버 장소 상세 정보 페이지 url
}
