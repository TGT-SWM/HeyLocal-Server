package com.heylocal.traveler.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MANAGER")
@DiscriminatorValue("MANAGER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Manager extends User {
  @Column(length = 20, nullable = false)
  private String realName;
}
