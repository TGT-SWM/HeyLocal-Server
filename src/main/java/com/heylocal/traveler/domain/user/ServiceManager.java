package com.heylocal.traveler.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("SERVICE_MANAGER")
@Table(name = "SERVICE_MANAGER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ServiceManager extends User {
  private String nickname;
}
