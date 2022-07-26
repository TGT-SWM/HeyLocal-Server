/**
 * packageName    : com.heylocal.traveler.domain.profile
 * fileName       : UserProfile
 * author         : 우태균
 * date           : 2022/10/02
 * description    : 사용자 프로필 엔티티
 */

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

  public void setActivityRegion(Region activityRegion) {
    Region tmp = this.activityRegion;

    //만약 동일한 region을 전달받은 경우
    if (tmp == activityRegion) return;

    this.activityRegion = activityRegion;
    if (tmp != null && tmp.getActivityUser().contains(this)) {
      tmp.removeActivityUser(this);
    }
  }

  public void releaseActivityRegion() {
    this.activityRegion = null;
  }

  public void increaseKnowHowBy(int amount) {
    knowHow += amount;
  }
}
