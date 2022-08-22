package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.visitreview.VisitReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRAVELER")
@DiscriminatorValue("TRAVELER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Traveler extends User {
  @Column(length = 20, nullable = false)
  private String nickname;

  //양방향 설정

  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<OrderSheet> orderSheetList = new ArrayList<>();

  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<VisitReview> visitReviewList = new ArrayList<>();
}
