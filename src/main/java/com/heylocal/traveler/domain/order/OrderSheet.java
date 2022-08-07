package com.heylocal.traveler.domain.order;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.order.list.HopeAccommodation;
import com.heylocal.traveler.domain.order.list.HopePlace;
import com.heylocal.traveler.domain.order.list.HopeTheme;
import com.heylocal.traveler.domain.order.list.TravelMember;
import com.heylocal.traveler.domain.travel.Travel;
import com.heylocal.traveler.domain.user.Traveler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  @ManyToOne
  private Traveler writer;

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

  //양방향 설정

  @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private List<HopeAccommodation> hopeAccommodationList = new ArrayList<>();

  @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private List<HopePlace> hopePlaceList = new ArrayList<>();

  @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private List<HopeTheme> hopeThemeList = new ArrayList<>();

  @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private List<TravelMember> travelMemberList = new ArrayList<>();

  @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private List<OrderRequest> orderRequestList = new ArrayList<>();

  @OneToOne(mappedBy = "orderSheet", cascade = CascadeType.ALL)
  private Travel travel;
}
