package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방
 */
@Entity
@Table(name = "CHAT_ROOM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class ChatRoom extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Travel travel;

  //양방향 설정

  @OneToMany(mappedBy= "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ChatMessage> chatMessageList = new ArrayList<>();
}
