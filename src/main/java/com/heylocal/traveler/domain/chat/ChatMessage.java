package com.heylocal.traveler.domain.chat;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Builder
public class ChatMessage extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private ChatRoom chatRoom;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChatMessageType type;

  @OneToOne
  @JoinColumn(nullable = false)
  private User sender;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean readMessage;
}
