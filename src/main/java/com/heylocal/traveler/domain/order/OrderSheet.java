package com.heylocal.traveler.domain.order;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 여행 의뢰서
 */

@Entity
@Table(name = "ORDER_SHEET")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderSheet extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Region region;

  @Column(nullable = false)
  private String travelStartAddress;

  @Column(nullable = false)
  private String travelEndAddress;

  @Column(nullable = false)
  private LocalDateTime travelStartTime;

  @Column(nullable = false)
  private LocalDateTime travelEndTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransportationType transportationType;

  private String additionalInfo;

  @Column(nullable = false)
  @ColumnDefault("true")
  private Boolean afterCare;

  private Integer cost;
}
