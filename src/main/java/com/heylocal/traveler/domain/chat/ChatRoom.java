/**
 * packageName    : com.heylocal.traveler.domain.chat
 * fileName       : ChatRoom
 * author         : 우태균
 * date           : 2022/09/19
 * description    : 채팅방 엔티티
 */

package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.*;
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
@Setter
@SuperBuilder
public class ChatRoom extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false)
  private User userA;

  @ManyToOne(optional = false)
  private User userB;

  @OneToOne
  private ChatMessage lastMessage;

  //양방향 설정
  @Builder.Default
  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ChatMessage> chatMessageList = new ArrayList<>();
}
