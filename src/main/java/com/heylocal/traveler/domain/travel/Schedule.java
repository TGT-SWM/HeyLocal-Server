package com.heylocal.traveler.domain.travel;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 스케줄표
 */
@Entity
@Table(name = "SCHEDULE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Schedule extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private LocalDateTime dateTime; //스케줄 날짜

  @ManyToOne
  @JoinColumn(nullable = false)
  private Travel travel;
}
