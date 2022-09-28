package com.heylocal.traveler.domain;

import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 시/도, 시
 */

@Entity
@Table(name = "REGION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Region {
  @Id @GeneratedValue
  private Long id;

  @Column(length = 20, nullable = false)
  private String state;

  @Column(length = 20)
  private String city;

  //양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Opinion> opinionList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Place> placeList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<TravelOn> travelOnList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "activityRegion", fetch = FetchType.LAZY)
  private List<UserProfile> activityUser = new ArrayList<>(); //주 활동 지역으로 갖는 유저 프로필

  public void addOpinion(Opinion opinion) {
    this.getOpinionList().add(opinion);
    if (opinion.getRegion() != this) {
      opinion.setRegion(this);
    }
  }

  public void removeOpinion(Opinion target) {
    this.getOpinionList().remove(target);
  }

  public String getRegionName() {
    if (city == null)
      return state;
    else
      return state + " " + city;
  }
}
