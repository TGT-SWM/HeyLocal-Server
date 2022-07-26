/**
 * packageName    : com.heylocal.traveler.domain.notification
 * fileName       : Notification
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 알림 엔티티
 */

package com.heylocal.traveler.domain.notification;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 알림
 */
@Entity
@Table(name = "NOTIFICATION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Notification extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false)
  private User receiver;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(nullable = false)
  private String message;
}
