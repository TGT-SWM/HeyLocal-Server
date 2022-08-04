package com.heylocal.traveler.domain.order;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.theme.Theme;
import com.heylocal.traveler.domain.place.Place;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행 의뢰서
 */
public class OrderSheet {
  private Long id;
  private Region region;
  private Place travelStartPlace; // TODO https://swm13-tgt.atlassian.net/browse/S3T-302
  private Place travelEndPlace; // TODO https://swm13-tgt.atlassian.net/browse/S3T-302
  private LocalDateTime travelStartTime;
  private LocalDateTime travelEndTime;
  private List<Member> memberList = new ArrayList<>();
  private List<Theme> themeList = new ArrayList<>();
  private List<AccommodationType> hopeAccommodationType = new ArrayList<>();
  private String accommodationInfo; //숙소 타입이 ETC 인 경우가 아니면 null
  private TransportationType transportationType;
  private List<Place> hopePlace = new ArrayList<>();
  private String additionalInfo;
  private List<PrecautionType> precautionTypeList = new ArrayList<>();
  private String precautionInfo;
  private Boolean afterCare;
  private Integer cost;


  /**
   * 여행 동행인
   */
  public static class Member {
    private Long id;
    private Gender gender;
    private Age age;

    public enum Gender {
      MAN, WOMAN
    }
    public enum Age {
      UNDER_10, UNDER_20, UNDER_30, UNDER_40, UNDER_50, UNDER_60, UNDER_70, UNDER_80, UNDER_90
    }
  }

  /**
   * 숙소 종류
   */
  public enum AccommodationType {
    HOTEL, RESORT, SHARE_HOUSE, PENSION, GUEST_HOUSE, ETC
  }

  /**
   * 자동차 여부 종류
   */
  public enum TransportationType {
    OWN_CAR, RENT_CAR, NO_CAR
  }

  /**
   * 주의사항 종류
   */
  public enum PrecautionType {
    ALLERGY("알러지가 있어요"), LIMITED_MOBILITY("거동이 불편해요");

    private String value;

    PrecautionType(String value) {
      this.value = value;
    }
  }
}
