package com.heylocal.traveler.domain.theme;

import com.heylocal.traveler.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 테마
 */

@Entity
@Table(name = "THEME")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Theme extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String imageAddress;
}
