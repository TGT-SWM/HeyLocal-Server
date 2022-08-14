package com.heylocal.traveler.domain.profile;

import com.heylocal.traveler.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 매니저 프로필
 */

@Entity
@Table(name = "MANAGER_PROFILE")
@DiscriminatorValue("MANAGER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ManagerProfile extends UserProfile {
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ManagerGrade grade;

  private String imageUrl;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer totalMatchNum;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ManagerResponseTime responseTime;

  @ManyToOne
  private Region activeRegion1;

  @ManyToOne
  private Region activeRegion2;

  @Column(nullable = false)
  @ColumnDefault("true")
  private Boolean activateReceiveMatch;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Float kindnessAvg;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Float responsivenessAvg;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Float noteDetailAvg;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Float notePrecisionAvg;

  private String introduction;
}
