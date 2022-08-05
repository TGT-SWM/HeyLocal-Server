package com.heylocal.traveler.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TRAVELER")
@DiscriminatorValue("TRAVELER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Traveler extends User {
  @Column(length = 20, nullable = false)
  private String nickname;
}
