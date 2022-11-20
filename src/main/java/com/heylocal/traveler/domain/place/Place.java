/**
 * packageName    : com.heylocal.traveler.domain.place
 * fileName       : Place
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 장소 엔티티
 */

package com.heylocal.traveler.domain.place;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.list.PlaceItem;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import lombok.*;
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
@Setter
@SuperBuilder
public class Place extends BaseTimeEntity {
  @Id
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PlaceCategory category;

  @Column(nullable = false)
  private String name;

  @Builder.Default
  @Column(nullable = true)
  private String roadAddress = ""; //도로명주소

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
      opinion.setPlace(this);
    }
  }

  public void removeOpinion(Opinion target) {
    this.getOpinionList().remove(target);
  }
}
