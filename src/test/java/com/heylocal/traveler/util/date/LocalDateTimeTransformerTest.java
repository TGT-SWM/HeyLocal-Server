package com.heylocal.traveler.util.date;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LocalDateTimeTransformerTest {
  private LocalDateTimeTransformer localDateTimeTransformer;

  @BeforeEach
  void setUp() {
    this.localDateTimeTransformer = new LocalDateTimeTransformer();
  }

  @Test
  @DisplayName("Date -> LocalDateTime")
  void dateToLocalDateTimeTest() {
    //GIVEN
    Date now = new Date();
    int dateYear = now.getYear() + 1900;
    int dateMonth = now.getMonth() + 1;
    int dateDate = now.getDate();
    int dateHour = now.getHours();
    int dateMinute = now.getMinutes();

    //WHEN
    LocalDateTime localDateTime = localDateTimeTransformer.dateToLocalDateTime(now);
    int localYear = localDateTime.getYear();
    int localMonth = localDateTime.getMonth().getValue();
    int localDate = localDateTime.getDayOfMonth();
    int localHour = localDateTime.getHour();
    int localMinute = localDateTime.getMinute();

    //THEN
    assertAll(
        () -> assertDoesNotThrow(() -> localDateTimeTransformer.dateToLocalDateTime(now)),
        () -> assertEquals(dateYear, localYear),
        () -> assertEquals(dateMonth, localMonth),
        () -> assertEquals(dateDate, localDate),
        () -> assertEquals(dateHour, localHour),
        () -> assertEquals(dateMinute, localMinute)
    );
  }

  @Test
  @DisplayName("Date -> LocalDate")
  void dateToLocalDateTest() {
    //GIVEN
    Date now = new Date();
    int dateYear = now.getYear() + 1900;
    int dateMonth = now.getMonth() + 1;
    int dateDate = now.getDate();

    //WHEN
    LocalDate localDate = localDateTimeTransformer.dateToLocalDate(now);
    int localYear = localDate.getYear();
    int localMonth = localDate.getMonth().getValue();
    int localDateDate = localDate.getDayOfMonth();

    //THEN
    assertAll(
        () -> assertDoesNotThrow(() -> localDateTimeTransformer.dateToLocalDate(now)),
        () -> assertEquals(dateYear, localYear),
        () -> assertEquals(dateMonth, localMonth),
        () -> assertEquals(dateDate, localDateDate)
    );
  }

  @Test
  @DisplayName("LocalDateTime -> Date")
  void localDateTimeToDateTest() {
    //GIVEN
    LocalDateTime now = LocalDateTime.now();
    int localYear = now.getYear();
    int localMonth = now.getMonth().getValue();
    int localDateDate = now.getDayOfMonth();
    int localHour = now.getHour();
    int localMinute = now.getMinute();

    //WHEN
    Date date = localDateTimeTransformer.localDateTimeToDate(now);
    int dateYear = date.getYear() + 1900;
    int dateMonth = date.getMonth() + 1;
    int dateDate = date.getDate();
    int dateHour = date.getHours();
    int dateMinute = date.getMinutes();

    //THEN
    assertAll(
        () -> assertDoesNotThrow(() -> localDateTimeTransformer.localDateTimeToDate(now)),
        () -> assertEquals(localYear, dateYear),
        () -> assertEquals(localMonth, dateMonth),
        () -> assertEquals(localDateDate, dateDate),
        () -> assertEquals(localHour, dateHour),
        () -> assertEquals(localMinute, dateMinute)
    );
  }

  @Test
  @DisplayName("LocalDate -> Date")
  void localDateToDateTest() {
    //GIVEN
    LocalDate now = LocalDate.now();
    int localYear = now.getYear();
    int localMonth = now.getMonth().getValue();
    int localDateDate = now.getDayOfMonth();

    //WHEN
    Date date = localDateTimeTransformer.localDateToDate(now);
    int dateYear = date.getYear() + 1900;
    int dateMonth = date.getMonth() + 1;
    int dateDate = date.getDate();

    //THEN
    assertAll(
        () -> assertDoesNotThrow(() -> localDateTimeTransformer.localDateToDate(now)),
        () -> assertEquals(localYear, dateYear),
        () -> assertEquals(localMonth, dateMonth),
        () -> assertEquals(localDateDate, dateDate)
    );
  }

}