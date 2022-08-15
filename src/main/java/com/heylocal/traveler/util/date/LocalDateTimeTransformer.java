package com.heylocal.traveler.util.date;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class LocalDateTimeTransformer {
  public LocalDateTime dateToLocalDateTime(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
    LocalDateTime localDateTime = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
    return localDateTime;
  }

  public LocalDate dateToLocalDate(Date date) {
    LocalDate localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
    return localDate;
  }

  public Date localDateTimeToDate(LocalDateTime localDateTime) {
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    Date date = Date.from(instant);
    return date;
  }

  public Date localDateToDate(LocalDate localDate) {
    Date date = java.sql.Date.valueOf(localDate);
    return date;
  }
}
