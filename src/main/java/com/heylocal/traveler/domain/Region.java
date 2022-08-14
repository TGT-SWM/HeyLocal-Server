package com.heylocal.traveler.domain;

import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.profile.ManagerProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Builder
public class Region {
  @Id @GeneratedValue
  private Long id;

  @Column(length = 20, nullable = false)
  private String state;

  @Column(length = 20)
  private String city;

  //양방향 설정

  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
  private List<OrderSheet> orderSheetList = new ArrayList<>();

  @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
  private List<Place> placeList = new ArrayList<>();

  @OneToMany(mappedBy = "activeRegion1") //region 이 제거되어도 profile은 제거되면 안되므로, cascade 설정 X
  private List<ManagerProfile> managerProfileList1 = new ArrayList<>();

  @OneToMany(mappedBy = "activeRegion2") //region 이 제거되어도 profile은 제거되면 안되므로, cascade 설정 X
  private List<ManagerProfile> managerProfileList2 = new ArrayList<>();
}
