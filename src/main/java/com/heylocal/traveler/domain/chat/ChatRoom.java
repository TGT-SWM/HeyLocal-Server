package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 채팅방
 */
@Entity
@Table(name = "CHAT_ROOM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @OneToOne
  @JoinColumn(nullable = false)
  private Travel travel;
}
