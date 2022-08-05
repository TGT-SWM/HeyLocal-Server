package com.heylocal.traveler.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 시/도, 시
 */

@Entity
@Table(name = "REGION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Region {
  @Id @GeneratedValue
  private Long id;

  @Column(length = 20, nullable = false)
  private String state;

  @Column(length = 20)
  private String city;
}
