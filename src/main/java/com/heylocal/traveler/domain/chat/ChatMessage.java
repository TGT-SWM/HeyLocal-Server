package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 채팅 메시지
 */

@Entity
@Table(name = "CHAT_MESSAGE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class ChatMessage extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(optional = false)
  private ChatRoom chatRoom;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChatMessageType type;

  @OneToOne(optional = false)
  private User sender;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean readMessage;
}
