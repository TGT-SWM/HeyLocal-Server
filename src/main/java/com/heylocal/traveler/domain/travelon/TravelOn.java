package com.heylocal.traveler.domain.travelon;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travel.Travel;
import com.heylocal.traveler.domain.travelon.list.HopeAccommodation;
import com.heylocal.traveler.domain.travelon.list.HopeDrink;
import com.heylocal.traveler.domain.travelon.list.HopeFood;
import com.heylocal.traveler.domain.travelon.list.TravelMember;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행 On
 */

@Entity
@Table(name = "TRAVEL_ON")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class TravelOn extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Lob
  private String description;

  @Column(nullable = false)
  @ColumnDefault("1")
  private Integer views;

  @ManyToOne(optional = false)
  private Region region;

  @ManyToOne(optional = false)
  private User author;

  @Column(nullable = false)
  private LocalDate travelStartDate;

  @Column(nullable = false)
  private LocalDate travelEndDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransportationType transportationType;

  @Column(nullable = false)
  private Integer accommodationMaxCost;

  @Column(nullable = false)
  private Integer foodMaxCost;

  @Column(nullable = false)
  private Integer drinkMaxCost;


  //양방향 설정

  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL)
  private List<TravelMember> travelMemberList = new ArrayList<>();

  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL)
  private List<HopeAccommodation> hopeAccommodationList = new ArrayList<>();

  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL)
  private List<HopeFood> hopeFoodList = new ArrayList<>();

  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL)
  private List<HopeDrink> hopeDrinkList = new ArrayList<>();

  @OneToOne(mappedBy = "travelOn", cascade = CascadeType.ALL)
  private TravelTypeGroup travelTypeGroup;

  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Opinion> opinionList = new ArrayList<>();

  @OneToOne(mappedBy = "travelOn")
  private Travel travel;

}