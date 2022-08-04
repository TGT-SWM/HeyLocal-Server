package com.heylocal.traveler.domain.profile;

/**
 * 매니저 평균 응답 시간 단위
 */
public enum ManagerResponseTime {
  MINUTE_1(1), MINUTE_10(10), HOUR_1(60), HOUR_2(120),
  HOUR_3(180), HOUR_4(240), HOUR_5(300), HOUR_6(360),
  HOUR_7(420), HOUR_8(480), HOUR_9(540), HOUR_10(600),
  HOUR_11(660), HOUR_12(720), HOUR_13(780), HOUR_14(840),
  HOUR_15(900), HOUR_16(960), HOUR_17(1020), HOUR_18(1080),
  HOUR_19(1140), HOUR_20(1200), HOUR_21(1260), HOUR_22(1320),
  HOUR_23(1380), HOUR_24(1440);

  private int value;

  ManagerResponseTime(int value) {
    this.value = value;
  }
}
