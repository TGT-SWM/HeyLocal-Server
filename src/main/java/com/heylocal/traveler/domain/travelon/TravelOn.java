/**
 * packageName    : com.heylocal.traveler.domain.travelon
 * fileName       : TravelOn
 * author         : 우태균
 * date           : 2022/09/20
 * description    : 여행On 엔티티
 */

package com.heylocal.traveler.domain.travelon;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.plan.Plan;
import com.heylocal.traveler.domain.travelon.list.HopeAccommodation;
import com.heylocal.traveler.domain.travelon.list.HopeDrink;
import com.heylocal.traveler.domain.travelon.list.HopeFood;
import com.heylocal.traveler.domain.travelon.list.TravelMember;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TRAVEL_ON")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TravelOn extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Lob
  private String description;

  @Column(nullable = false)
  @ColumnDefault("0")
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

  //양방향 설정

  @Builder.Default
  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TravelMember> travelMemberSet = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HopeAccommodation> hopeAccommodationSet = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HopeFood> hopeFoodSet = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HopeDrink> hopeDrinkSet = new ArrayList<>();

  @OneToOne(mappedBy = "travelOn", cascade = CascadeType.ALL, orphanRemoval = true)
  private TravelTypeGroup travelTypeGroup;

  @Builder.Default
  @OneToMany(mappedBy = "travelOn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Opinion> opinionList = new ArrayList<>();

  @OneToOne(mappedBy = "travelOn", fetch = FetchType.LAZY)
  private Plan plan;

  public void addTravelMember(TravelMember travelMember) {
    this.travelMemberSet.add(travelMember);
    if (travelMember.getTravelOn() != this) {
      travelMember.registerAt(this);
    }
  }

  public void addHopeAccommodation(HopeAccommodation hopeAccommodation) {
    this.hopeAccommodationSet.add(hopeAccommodation);
    if (hopeAccommodation.getTravelOn() != this) {
      hopeAccommodation.registerAt(this);
    }
  }

  public void addHopeFood(HopeFood hopeFood) {
    this.hopeFoodSet.add(hopeFood);
    if (hopeFood.getTravelOn() != this) {
      hopeFood.registerAt(this);
    }
  }

  public void addHopeDrink(HopeDrink hopeDrink) {
    this.hopeDrinkSet.add(hopeDrink);
    if (hopeDrink.getTravelOn() != this) {
      hopeDrink.registerAt(this);
    }
  }

  public void registerTravelTypeGroup(TravelTypeGroup travelTypeGroup) {
    this.travelTypeGroup = travelTypeGroup;
    if (travelTypeGroup.getTravelOn() != this) {
      travelTypeGroup.registerAt(this);
    }
  }

  public void addOpinion(Opinion opinion) {
    this.opinionList.add(opinion);
    if (opinion.getTravelOn() != this) {
      opinion.setTravelOn(this);
    }
  }

  public void removeAllHopeAccommodation() {
    this.hopeAccommodationSet.clear();
  }

  public void removeAllHopeFood() {
    this.hopeFoodSet.clear();
  }

  public void removeAllHopeDrink() {
    this.hopeDrinkSet.clear();
  }

  public void removeAllTravelMember() {
    this.travelMemberSet.clear();
  }

  public void removeOpinion(Opinion target) {
    this.opinionList.remove(target);
  }

  public void releasePlan() {
    Plan temp = this.plan;
    this.plan = null;

    if (temp != null) {
      temp.releaseTravelOn();
    }
  }

  public void incrViewsByOne() {
    this.views++;
  }
}
