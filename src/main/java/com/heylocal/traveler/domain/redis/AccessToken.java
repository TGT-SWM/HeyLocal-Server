package com.heylocal.traveler.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@RedisHash("AccessToken")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccessToken {
  @Id
  private Long userId;
  private String tokenValue;
  @TimeToLive
  private Long timeoutSec;
  private Long refreshTokenId;
  private LocalDateTime createdDate = LocalDateTime.now();

  public AccessToken(Long userId, String tokenValue, Long timeoutSec, Long refreshTokenId) {
    this.userId = userId;
    this.tokenValue = tokenValue;
    this.timeoutSec = timeoutSec;
    this.refreshTokenId = refreshTokenId;
  }


}
