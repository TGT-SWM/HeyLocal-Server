package com.heylocal.traveler.domain.travel;

import com.heylocal.traveler.domain.place.Place;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 스케줄표
 * TODO - 장소 오더링
 */
public class Schedule {
  private Long id;
  private LocalDateTime dateTime; //스케줄 날짜
  private List<Place> placeList; //방문 장소

  /**
   * 장소+순서
   */
  public static class PlaceItem {
    private Place place;
    private Integer order; //오름차순
  }
}
