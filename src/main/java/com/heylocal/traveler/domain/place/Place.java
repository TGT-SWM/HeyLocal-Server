package com.heylocal.traveler.domain.place;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
  @Id @GeneratedValue
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

  @ManyToOne
  @JoinColumn(nullable = false)
  private Region region;

  private String imageUrl;

  @Column(nullable = false)
  private String link; //카카오 장소 상세 정보 페이지 url
}
