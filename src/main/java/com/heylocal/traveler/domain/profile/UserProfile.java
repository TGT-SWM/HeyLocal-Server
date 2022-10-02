package com.heylocal.traveler.domain.profile;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 사용자 프로필
 */

@Entity
@Table(name = "USER_PROFILE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserProfile extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  private String introduce;

  private String imageObjectKeyName;

  @ManyToOne
  private Region activityRegion;

  @OneToOne(optional = false)
  private User user;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer knowHow; //Redis로 이동해야함

  public void setUser(User user) {
    this.user = user;
    if (user.getUserProfile() != this) {
      user.setUserProfile(this);
    }
  }
}
