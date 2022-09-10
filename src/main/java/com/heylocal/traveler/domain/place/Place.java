package com.heylocal.traveler.domain.place;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 장소 (방문지)
 */

@Entity
@Table(name = "PLACE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Place extends BaseTimeEntity {
  @Id
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceCategory category;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String roadAddress; //도로명주소

  @Column(nullable = false)
  private String address; //구주소

  @Column(nullable = false)
  private Double lat;

  @Column(nullable = false)
  private Double lng;

  @ManyToOne(optional = false)
  private Region region;

  private String thumbnailUrl;

  @Column(nullable = false)
  private String link; //카카오 장소 상세 정보 페이지 url

  // 양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Opinion> opinionList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PlaceItem> placeItemList = new ArrayList<>();

  public void addOpinion(Opinion opinion) {
    this.opinionList.add(opinion);
    if (opinion.getPlace() != this) {
      opinion.registerPlace(this);
    }
  }


  public void updateName(String newName) {
    this.name = newName;
  }
  public void updateRoadAddress(String newRoadAddress) {
    this.roadAddress = roadAddress;
  }

  public void updateAddress(String newAddress) {
    this.address = newAddress;
  }

  public void updateCoordinates(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public void updateCategory(PlaceCategory category) {
    this.category = category;
  }

  public void updateRegion(Region region) {
    this.region = region;
  }

  public void updateThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public void updateLink(String link) {
    this.link = link;
  }
}
