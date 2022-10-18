package com.heylocal.traveler.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@RedisHash("RefreshToken")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {
  @Id
  private Long userId;
  private String tokenValue;
  @TimeToLive
  private Long timeoutSec;
  private Long accessTokenId;
  @Builder.Default
  private LocalDateTime createdDate = LocalDateTime.now();

  public RefreshToken(Long userId, String tokenValue, Long timeoutSec, Long accessTokenId) {
    this.userId = userId;
    this.tokenValue = tokenValue;
    this.timeoutSec = timeoutSec;
    this.accessTokenId = accessTokenId;
  }

  public void associateAccessToken(AccessToken accessToken) {
    this.accessTokenId = accessToken.getUserId();
    if (accessToken.getRefreshTokenId() != this.getUserId()) {
      accessToken.associateRefreshToken(this);
    }
  }
}
